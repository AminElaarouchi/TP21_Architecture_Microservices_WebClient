package com.example.tp21.car;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final ClientApi clientApi;

    public TestController(ClientApi clientApi) { this.clientApi = clientApi; }

    @GetMapping("/client/{id}")
    public ClientDto testClient(@PathVariable Long id) { return clientApi.findClientById(id); }
}
