package com.example.tp21.car;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marque;
    private String modele;
    
    private Long clientId;
    @Transient
    private ClientDto client;

    protected Car() { }

    public Car(Long id, String marque, String modele, Long clientId) {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        
        this.clientId = clientId;
    }

    public Long getId() { return id; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    
    public Long getClientId() { return clientId; }
    public ClientDto getClient() { return client; }
    public void setClient(ClientDto client) { this.client = client; }
}
