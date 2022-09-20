package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.PlanetUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/planet")
public class PlanetController {

    private final @NonNull PlanetUseCase planetUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planet createPlanet(@Valid @RequestBody PlanetDTO planetDTO) {
        return planetUseCase.createPlanet(planetDTO);
    }

    @GetMapping("")
    public Slice<Planet> getAllPlanets(@RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> pageSize) {
        if (pageNumber.isPresent() && pageSize.isPresent()) {
            return planetUseCase.getAllPlanets(pageNumber.get(), pageSize.get());
        } else return planetUseCase.getAllPlanets();
    }

    @GetMapping("/{id}")
    public Planet getPlanetById(@PathVariable(value = "id") long planetId) {
        return planetUseCase.getPlanetById(planetId);
    }

    @GetMapping("/{id}/rockets")
    public Slice<Rocket> getRocketsInPlanet(@PathVariable(value = "id") long planetId, @RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> pageSize) {
        if (pageNumber.isPresent() && pageSize.isPresent()) {
            return planetUseCase.getRocketsInPlanet(planetId, pageNumber.get(), pageSize.get());
        } else return planetUseCase.getRocketsInPlanet(planetId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlanetById(@Validated @Positive @PathVariable(value = "id") long planetId) {
        planetUseCase.deletePlanet(planetId);
    }
}
