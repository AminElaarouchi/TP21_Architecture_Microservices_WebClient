package com.example.tp21.client;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientRepository repository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Client> findAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client introuvable : " + id));
    }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client input) {
        Client saved = repository.save(new Client(null, input.getNom(), input.getAge()));
        return ResponseEntity.created(URI.create("/api/clients/" + saved.getId())).body(saved);
    }
}
