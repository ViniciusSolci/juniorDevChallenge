package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.*;
import com.elo7.junior.dev.challenge.repository.PlanetRepository;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class RocketUseCase {
    private static final String ROCKET_PLANET = "Invalid rocket id (rocket id < 0)";

    private final @NonNull RocketRepository rocketRepository;

    private final @NonNull PlanetRepository planetRepository;

    private final @NonNull RocketMovementValidator rocketMovementValidator;

    private final @NonNull RocketMovementHandler rocketMovementHandler;

    public Rocket createRocket() {
        Rocket rocket = new Rocket();
        rocket.setAllocatedPlanetId(0);
        Point point = new Point(0, 0);
        rocket.setCoordinates(point);

        return rocketRepository.save(rocket);
    }

    public Slice<Rocket> getAllRockets(int pageNumber, int pageSize) {
        Pageable sortedById = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return rocketRepository.findAll(sortedById);
    }

    public Slice<Rocket> getAllRockets() {
        return rocketRepository.findAll(Pageable.unpaged());
    }

    public Rocket getRocketById(long rocketId) {
        if (rocketId < 0) {
            throw new InvalidParameterException(ROCKET_PLANET);
        }
        try {
            return rocketRepository.findById(rocketId).orElseThrow();
        } catch (NoSuchElementException exception) {
            throw new NotFoundException("Rocket with id: " + rocketId + " does not exists");
        }
    }

    public Rocket sendToPlanet(long rocketId, long planetId) {
        Rocket rocket = getRocketById(rocketId);

        if (!planetRepository.existsById(planetId))
            throw new NotFoundException("Planet with id: " + planetId + " does not exists");

        if (rocket.getAllocatedPlanetId() == planetId) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at destiny planet");
        } else if (rocket.getAllocatedPlanetId() != 0) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at planet. Rocket must be recalled before sent to a new planet");
        }

        rocketMovementValidator.validatedLandingPosition(rocket, planetId);
        rocket.setAllocatedPlanetId(planetId);
        rocket.setFacingDirection(Rocket.FACING_DIRECTION.NORTH);

        return rocketRepository.save(rocket);
    }

    public Rocket recallRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() != 0) {
            rocket.setAllocatedPlanetId(0);
            return rocketRepository.save(rocket);
        } else
            throw new UnallocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket is not allocated. Only allocated rockets can be recalled");
    }

    public void destroyRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() == 0) {
            rocketRepository.delete(rocket);
        } else
            throw new AllocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket must be recalled before it's destroyed");
    }

    public Rocket moveRocketById(long rocketId, String movementList) {
        Rocket rocket = getRocketById(rocketId);
        for (String movement : movementList.split("")) {
            switch (movement.toLowerCase()) {
                case "m":
                    rocketMovementHandler.moveRocketForward(rocket);
                    break;
                case "l":
                    rocketMovementHandler.turnRocketLeft(rocket);
                    break;
                case "r":
                    rocketMovementHandler.turnRocketRight(rocket);
                    break;
                default:
                    throw new UnacceptableMovementCommandException(HttpStatus.NOT_ACCEPTABLE, "Unacceptable movement command was found in the movement list");
            }
        }

        return rocketRepository.save(rocket);
    }
}
