package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.*;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RocketUseCase {

    private final @NonNull RocketRepository rocketRepository;

    private final @NonNull RocketMovementValidator rocketMovementValidator;

    public Rocket createRocket() {
        Rocket rocket = new Rocket();
        rocket.setAllocatedPlanetId(0);
        Point point = new Point(0, 0);
        rocket.setCoordinates(point);

        return rocketRepository.save(rocket);
    }

    public List<Rocket> getAllRockets() {
        return rocketRepository.findAll();
    }

    public Rocket getRocketById(long rocketId) {
        return rocketRepository.findById(rocketId).orElseThrow();
    }

    public Rocket moveRocketById(long rocketId, String movementList) {
        Rocket rocket = getRocketById(rocketId);
        if (rocket.getAllocatedPlanetId() == 0) {
            throw new UnallocatedRocketException(HttpStatus.UNPROCESSABLE_ENTITY, "Rocket must be allocated before moving");
        }
        for (String movement : movementList.split("")) {
            switch (movement.toLowerCase()) {
                case "m":
                    moveRocketForward(rocket);
                    break;
                case "l":
                    turnRocketLeft(rocket);
                    break;
                case "r":
                    turnRocketRight(rocket);
                    break;
                default:
                    throw new UnacceptableMovementCommandException(HttpStatus.NOT_ACCEPTABLE, "Unacceptable movement command was found in the movement list");
            }
        }

        return rocketRepository.save(rocket);
    }

    private void turnRocketRight(Rocket rocket) {
        switch (rocket.getFacingDirection()) {
            case NORTH:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.EAST);
                break;
            case SOUTH:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.WEST);
                break;
            case EAST:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.SOUTH);
                break;
            case WEST:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.NORTH);
                break;
            default:
                break;
        }
    }

    private void turnRocketLeft(Rocket rocket) {
        switch (rocket.getFacingDirection()) {
            case NORTH:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.WEST);
                break;
            case SOUTH:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.EAST);
                break;
            case EAST:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.NORTH);
                break;
            case WEST:
                rocket.setFacingDirection(Rocket.FACING_DIRECTION.SOUTH);
                break;
            default:
                break;
        }
    }

    private void moveRocketForward(Rocket rocket) {
        Point point = new Point();
        switch (rocket.getFacingDirection()) {
            case NORTH:
                point.setLocation(rocket.getXCoordinate() + 1, rocket.getYCoordinate());
                break;
            case SOUTH:
                point.setLocation(rocket.getXCoordinate() - 1, rocket.getYCoordinate());
                break;
            case EAST:
                point.setLocation(rocket.getXCoordinate(), rocket.getYCoordinate() + 1);
                break;
            case WEST:
                point.setLocation(rocket.getXCoordinate() + 1, rocket.getYCoordinate() - 1);
                break;
            default:
                break;
        }
        if (!rocketMovementValidator.validatedMovement(rocket.getAllocatedPlanetId(), point)) {
            rocket.setCoordinates(point);
        } else
            throw new UnacceptableRocketMovementException(HttpStatus.CONFLICT, "Rocket movement is blocked by another rocket or planet boundaries");

    }

    public Rocket sendToPlanet(long rocketId, long planetId) {
        Rocket rocket = getRocketById(rocketId);
        if (rocket.getAllocatedPlanetId() == planetId) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.UNPROCESSABLE_ENTITY, "Rocket already allocated at destiny planet");
        } else if (rocket.getAllocatedPlanetId() != 0) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.UNPROCESSABLE_ENTITY, "Rocket already allocated at planet. Rocket must be recalled before sent to a new planet");
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
            throw new UnallocatedRocketException(HttpStatus.UNPROCESSABLE_ENTITY, "Rocket is not allocated. Only allocated rockets can be recalled");
    }

    public void destroyRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() == 0) {
            rocketRepository.delete(rocket);
        } else
            throw new AllocatedRocketException(HttpStatus.UNPROCESSABLE_ENTITY, "Rocket must be recalled before it's destroyed");
    }
}
