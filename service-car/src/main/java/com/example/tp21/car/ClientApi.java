package com.example.tp21.car;

import java.time.Duration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClientApi {
    private final WebClient client;

    public ClientApi(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://SERVICE-CLIENT").build();
    }

    public ClientDto findClientById(Long id) {
        return client.get()
                .uri("/api/clients/{id}", id)
                .retrieve()
                .bodyToMono(ClientDto.class)
                .block(Duration.ofSeconds(5));
    }
}
