package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.usecase.RocketUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rocket")
public class RocketController {

    private final @NonNull RocketUseCase rocketUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rocket createRocket() {
        return rocketUseCase.createRocket();
    }

    @GetMapping
    public Slice<Rocket> getAllRockets(@RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> pageSize) {
        if (pageNumber.isPresent() && pageSize.isPresent()) {
            return rocketUseCase.getAllRockets(pageNumber.get(), pageSize.get());
        } else return rocketUseCase.getAllRockets();
    }

    @GetMapping("/{id}")
    public Rocket getRocketById(
            @PathVariable(value = "id") long rocketId) {
        return rocketUseCase.getRocketById(rocketId);
    }

    @PostMapping("/{id}/move")
    public Rocket moveRocketBy(
            @PathVariable(value = "id") long rocketId,
            @Valid @RequestBody RocketMovementDTO request) {
        return rocketUseCase.moveRocketById(rocketId, request.getMovementList());
    }

    @PostMapping("/{id}/sendToPlanet/{planetId}")
    public Rocket sendToPlanet(
            @PathVariable(value = "id") long rocketId,
            @PathVariable(value = "planetId") long planetId) {
        return rocketUseCase.sendToPlanet(rocketId, planetId);
    }

    @PostMapping("/{id}/recall")
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
