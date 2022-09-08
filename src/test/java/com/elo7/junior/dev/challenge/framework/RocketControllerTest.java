package com.elo7.junior.dev.challenge.framework;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RocketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createRocket() throws Exception {
        mockMvc.perform(post("/v1/rocket"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allocatedPlanetId", Matchers.is(0)))
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    void getAllRockets() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/rocket"));

        mockMvc.perform(get("/v1/rocket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    void getRocketById() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/rocket"));

        mockMvc.perform(get("/v1/rocket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));

        mockMvc.perform(get("/v1/rocket/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(2)));
    }

    @Test
    void moveRocketById() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"size\":\"5x5\"}"));

        mockMvc.perform(post("/v1/rocket/1/sendToPlanet/2"));
        mockMvc.perform(post("/v1/rocket/1/move/MLrmlR"))
                .andExpect(jsonPath("$.allocatedPlanetId", Matchers.is(2)))
                .andExpect(jsonPath("$.facingDirection", Matchers.is("NORTH")));

    }

    @Test
    void sendToPlanet() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"size\":\"5x5\"}"));

        mockMvc.perform(post("/v1/rocket/1/sendToPlanet/2"))
                .andExpect(jsonPath("$.allocatedPlanetId", Matchers.is(2)));
    }

    @Test
    void recallRocket() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(post("/v1/planet").contentType("application/json").content("{\"name\":\"Planet\", \"size\":\"5x5\"}"));

        mockMvc.perform(post("/v1/rocket/1/sendToPlanet/2"))
                .andExpect(jsonPath("$.allocatedPlanetId", Matchers.is(2)));

        mockMvc.perform(post("/v1/rocket/1/recall"))
                .andExpect(jsonPath("$.allocatedPlanetId", Matchers.is(0)));
    }

    @Test
    void destroyRocket() throws Exception {
        mockMvc.perform(post("/v1/rocket"));
        mockMvc.perform(delete("/v1/rocket/1")).andExpect(status().isNoContent());
    }
}