package com.example.tp21.car;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarRepository repository;
    private final ClientApi clientApi;

    public CarController(CarRepository repository, ClientApi clientApi) {
        this.repository = repository;
        this.clientApi = clientApi;
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody Car input) {
        Car saved = repository.save(new Car(null, input.getMarque(), input.getModele(), input.getClientId()));
        return ResponseEntity.created(URI.create("/api/cars/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Car> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @GetMapping("/byClient/{clientId}")
    public List<Car> findByClient(@PathVariable Long clientId) {
        return repository.findByClientId(clientId).stream().map(this::enrich).toList();
    }

    private Car enrich(Car car) {
        if (car.getClientId() != null) {
            car.setClient(clientApi.findClientById(car.getClientId()));
        }
        return car;
    }
}
