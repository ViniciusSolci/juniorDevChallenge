package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.PlanetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/planet")
public class PlanetController {

    PlanetUseCase planetUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planet createPlanet(@Valid @RequestBody PlanetDTO planetDTO) {
        return planetUseCase.createPlanet(planetDTO);
    }

    @GetMapping
    public List<Planet> getAllPlanets() {
        return planetUseCase.getAllPlanets();
    }

    @GetMapping("/{id}")
    public Planet getPlanetById(@PathVariable(value = "id") long planetId) {
        return planetUseCase.getPlanetById(planetId);
    }

    @GetMapping("/{id}/rockets")
    public List<Rocket> getRocketsInPlanet(@PathVariable(value = "id") long planetId) {
        return planetUseCase.getRocketsInPlanet(planetId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlanetById(@Validated @Positive @PathVariable(value = "id") long planetId) {
        planetUseCase.deletePlanet(planetId);
    }
}
