package com.example.tp21.car;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {"spring.profiles.active=demo", "eureka.client.enabled=false"})
@AutoConfigureMockMvc
class CarControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean ClientApi clientApi;

    @Test
    void returnsCarsEnrichedWithRemoteClient() throws Exception {
        when(clientApi.findClientById(1L)).thenReturn(new ClientDto(1L, "Salma", 22f));
        mvc.perform(post("/api/cars").contentType(MediaType.APPLICATION_JSON)
                .content("{\"marque\":\"Toyota\",\"modele\":\"Yaris\",\"clientId\":1}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].client.nom").value("Salma"));
        mvc.perform(get("/api/cars/byClient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modele").value("Yaris"));
    }
}
