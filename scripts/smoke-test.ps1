$ErrorActionPreference = 'Stop'
$root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$logs = Join-Path $root 'target/smoke-test'
New-Item -ItemType Directory -Force -Path $logs | Out-Null
$started = @()

function Start-ServiceJar([string]$module, [string]$profile = '') {
    $jar = Join-Path $root "$module/target/$module-1.0-SNAPSHOT.jar"
    if (-not (Test-Path $jar)) { throw "JAR absent: $jar. Lancez mvn package." }
    $arguments = @('-jar', $jar)
    if ($profile) { $arguments += "--spring.profiles.active=$profile" }
    $process = Start-Process -FilePath 'java' -ArgumentList $arguments -PassThru -WindowStyle Hidden `
        -RedirectStandardOutput (Join-Path $logs "$module.log") `
        -RedirectStandardError (Join-Path $logs "$module.err.log")
    $script:started += $process
}

function Wait-Endpoint([string]$url, [int]$seconds = 120) {
    $deadline = (Get-Date).AddSeconds($seconds)
    do {
        try { return Invoke-RestMethod -Uri $url -TimeoutSec 3 }
        catch { Start-Sleep -Seconds 2 }
    } while ((Get-Date) -lt $deadline)
    throw "Délai dépassé pour $url. Voir $logs"
}

try {
    Start-ServiceJar 'eureka-server'
    Wait-Endpoint 'http://localhost:8761/' | Out-Null
    Start-ServiceJar 'service-client' 'demo'
    Wait-Endpoint 'http://localhost:8081/api/clients' | Out-Null
    Start-ServiceJar 'service-car' 'demo'
    Wait-Endpoint 'http://localhost:8082/api/cars' | Out-Null

    $client = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/clients' `
        -ContentType 'application/json' -Body '{"nom":"Salma","age":22}'
    $body = @{ marque = 'Toyota'; modele = 'Yaris'; clientId = $client.id } | ConvertTo-Json -Compress
    $car = Invoke-RestMethod -Method Post -Uri 'http://localhost:8082/api/cars' `
        -ContentType 'application/json' -Body $body
    $result = Wait-Endpoint "http://localhost:8082/api/cars/byClient/$($client.id)"
    if (@($result).Count -ne 1 -or @($result)[0].client.nom -ne 'Salma') {
        throw "Réponse inattendue: $($result | ConvertTo-Json -Compress)"
    }
    Write-Output ($result | ConvertTo-Json -Depth 5 -Compress)
    Write-Output 'TP21 end-to-end: OK (Eureka, WebClient, LoadBalancer, H2)'
}
finally {
    foreach ($process in $started) {
        Stop-Process -Id $process.Id -Force -ErrorAction SilentlyContinue
    }
}
