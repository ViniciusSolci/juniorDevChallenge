package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.PlanetUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planet/v1")
public class PlanetResource {

    PlanetUseCase planetUseCase;

    @PostMapping("")
    public Planet createPlanet(@Validated @RequestBody PlanetDTO planetDTO) {
        return planetUseCase.createPlanet(planetDTO);
    }

    @GetMapping("")
    public List<Planet> getAllPlanets() {
        return planetUseCase.getAllPlanets();
    }

    @GetMapping("/{id}")
    public Planet getPlanetById(@PathVariable(value = "id") long planetId) {
        return planetUseCase.getPlanetById(planetId);
    }

    @GetMapping("/{id}/getRocketsInPlanet")
    public List<Rocket> getRocketsInPlanet(@PathVariable(value = "id") long planetId) {
        return planetUseCase.getRocketsInPlanet(planetId);
    }

    @DeleteMapping("/{id}")
    public String deletePlanetById(@PathVariable(value = "id") long planetId) throws Exception {
        return planetUseCase.deletePlanet(planetId);
    }
}
