package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.RocketUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rocket/v1")
public class RocketResource {

    RocketUseCase rocketUseCase;

    @PostMapping("")
    public Rocket createRocket() {
        return rocketUseCase.createRocket();
    }

    @GetMapping("")
    public List<Rocket> getAllRockets() {
        return rocketUseCase.getAllRockets();
    }

    @GetMapping("/{id}")
    public Rocket getRocketById(
            @PathVariable(value = "id") long rocketId) {
        return rocketUseCase.getRocketById(rocketId);
    }

    @PutMapping("/{id}/moveRocket/{movementList}")
    public Rocket moveRocketBy(
            @PathVariable(value = "id") long rocketId,
            @PathVariable(value = "movementList") String movementList) {
        return rocketUseCase.moveRocketById(rocketId, movementList);
    }

    @PutMapping("/{id}/sendToPlanet/{planetId}")
    public Rocket sendToPlanet(
            @PathVariable(value = "id") long rocketId,
            @PathVariable(value = "planetId") long planetId) {
        return rocketUseCase.sendToPlanet(rocketId, planetId);
    }

    @PutMapping("/{id}/recallRocket")
    public Rocket recallRocket(
            @PathVariable(value = "id") long rocketId) {
        return rocketUseCase.recallRocket(rocketId);
    }

    @DeleteMapping("/{id}")
    public String destroyRocket(@PathVariable(value = "id") long rocketId) {
        return rocketUseCase.destroyRocket(rocketId);
    }
}
