package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.RocketUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rocket")
public class RocketController {

    RocketUseCase rocketUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rocket createRocket() {
        return rocketUseCase.createRocket();
    }

    @GetMapping
    public List<Rocket> getAllRockets() {
        return rocketUseCase.getAllRockets();
    }

    @GetMapping("/{id}")
    public Rocket getRocketById(
            @PathVariable(value = "id") long rocketId) {
        return rocketUseCase.getRocketById(rocketId);
    }

    @PostMapping("/{id}/move/{movementList}")
    public Rocket moveRocketBy(
            @PathVariable(value = "id") long rocketId,
            @PathVariable(value = "movementList") List<Rocket.MOVEMENT_TYPE> movementList) {
        return rocketUseCase.moveRocketById(rocketId, movementList);
    }

    @PutMapping("/{id}/sendToPlanet/{planetId}")
    public Rocket sendToPlanet(
            @PathVariable(value = "id") long rocketId,
            @PathVariable(value = "planetId") long planetId) {
        return rocketUseCase.sendToPlanet(rocketId, planetId);
    }

    @PutMapping("/{id}/recall")
    public Rocket recallRocket(
            @PathVariable(value = "id") long rocketId) {
        return rocketUseCase.recallRocket(rocketId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroyRocket(@PathVariable(value = "id") long rocketId) {
        rocketUseCase.destroyRocket(rocketId);
    }
}