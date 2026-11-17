package com.example.tp21.client;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {"spring.profiles.active=demo", "eureka.client.enabled=false"})
@AutoConfigureMockMvc
class ClientControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void createsAndFindsClient() throws Exception {
        mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Salma\",\"age\":22}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Salma"));
        mvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Salma"));
    }
}
