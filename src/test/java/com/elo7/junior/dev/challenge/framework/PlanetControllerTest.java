package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Planet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlanetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createPlanet() throws Exception {
        Planet planet = new Planet();
        planet.setId(1);
        planet.setName("planet");
        Point point = new Point(5, 5);
        planet.setSize(point);

        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"planet\", \"length\":5, \"width\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("planet")))
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    void getAllPlanets() throws Exception {
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"length\":5, \"width\":5}"));
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet1\", \"length\":5, \"width\":5}"));

        mockMvc.perform(get("/v1/planet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content.[0].name", Matchers.is("Planet")))
                .andExpect(jsonPath("$.content.[1].name", Matchers.is("Planet1")));
    }

    @Test
    void getPlanetById() throws Exception {
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"length\":5, \"width\":5}"));
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet1\", \"length\":5, \"width\":5}"));

        mockMvc.perform(get("/v1/planet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Planet")));

        mockMvc.perform(get("/v1/planet/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Planet1")));

    }

    @Test
    void getRocketsInPlanet() throws Exception {
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"length\":5, \"width\":5}"));
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/rocket/2/sendToPlanet/1"));

        mockMvc.perform(get("/v1/planet/1/rockets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id", Matchers.is(2)));
    }

    @Test
    void deletePlanetById() throws Exception {
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"length\":5, \"width\":5}"));
        mockMvc.perform(delete("/v1/planet/1")).andExpect(status().isNoContent());
    }
}