package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.*;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class RocketUseCase {
    @Autowired
    private RocketRepository rocketRepository;

    @Autowired
    private PlanetUseCase planetUseCase;

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
        return rocketRepository.getReferenceById(rocketId);
    }

    public Rocket moveRocketById(long rocketId, String movementList) {
        Rocket rocket = getRocketById(rocketId);

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

    private static void turnRocketRight(Rocket rocket) {
        switch (rocket.getFacingDirection().toLowerCase()) {
            case "n":
                rocket.setFacingDirection("e");
                break;
            case "s":
                rocket.setFacingDirection("w");
                break;
            case "e":
                rocket.setFacingDirection("s");
                break;
            case "w":
                rocket.setFacingDirection("n");
                break;
            default:
                break;
        }
    }

    private static void turnRocketLeft(Rocket rocket) {
        switch (rocket.getFacingDirection().toLowerCase()) {
            case "n":
                rocket.setFacingDirection("w");
                break;
            case "s":
                rocket.setFacingDirection("e");
                break;
            case "e":
                rocket.setFacingDirection("n");
                break;
            case "w":
                rocket.setFacingDirection("s");
                break;
            default:
                break;
        }
    }

    private void moveRocketForward(Rocket rocket) {
        Point point = new Point();
        switch (rocket.getFacingDirection().toLowerCase()) {
            case "n":
                point.setLocation(rocket.getCoordinates().getX() + 1, rocket.getCoordinates().getY());
                break;
            case "s":
                point.setLocation(rocket.getCoordinates().getX() - 1, rocket.getCoordinates().getY());
                break;
            case "e":
                point.setLocation(rocket.getCoordinates().getX(), rocket.getCoordinates().getY() + 1);
                break;
            case "w":
                point.setLocation(rocket.getCoordinates().getX() + 1, rocket.getCoordinates().getY() - 1);
                break;
            default:
                break;
        }
        if (!validatedMovement(rocket.getAllocatedPlanetId(), point)) {
            rocket.setCoordinates(point);
        } else
            throw new UnacceptableRocketMovementException(HttpStatus.CONFLICT, "Rocket movement is blocked by another rocket or planet boundaries");

    }

    public Rocket sendToPlanet(long rocketId, long planetId) {
        Rocket rocket = getRocketById(rocketId);
        if (rocket.getAllocatedPlanetId() == planetId) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at destiny planet");
        } else if (rocket.getAllocatedPlanetId() != 0) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at planet. Rocket must be recalled before sent to a new planet");
        }

        validatedLandingPosition(rocket, planetId);
        rocket.setAllocatedPlanetId(planetId);
        rocket.setFacingDirection("n");

        return rocketRepository.save(rocket);
    }

    private void validatedLandingPosition(Rocket rocket, long planetId) {
        List<Point> rocketCoordinatesList = planetUseCase.getRocketsInPlanet(planetId).stream().map(Rocket::getCoordinates).collect(Collectors.toList());
        Planet planet = planetUseCase.getPlanetById(planetId);
        String[] planetSize = planet.getSize().split("");
        int planetXSize = Integer.parseInt(planetSize[0]);
        int planetYSize = Integer.parseInt(planetSize[1]);

        for (int i = 0; i < planetXSize; i++) {
            for (int j = 0; j < planetYSize; j++) {
                Point point = new Point(i, j);
                if (!rocketCoordinatesList.contains(point)) {
                    rocket.setCoordinates(point);
                    return;
                }
            }
        }

        throw new NoSpaceLeftForLandingsException(HttpStatus.NOT_ACCEPTABLE, "Planet is already full with rockets");
    }

    public Rocket recallRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() != 0) {
            rocket.setAllocatedPlanetId(0);
            return rocketRepository.save(rocket);
        } else
            throw new UnallocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket is not allocated. Only allocated rockets can be recalled");
    }

    public String destroyRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() == 0) {
            rocketRepository.delete(rocket);
            return "Rocket successfully destroyed";
        } else
            throw new AllocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket must be recalled before it's destroyed");
    }

    private boolean validatedMovement(long planetId, Point point) {
        List<Point> rocketCoordinatesList = planetUseCase.getRocketsInPlanet(planetId).stream().map(Rocket::getCoordinates).collect(Collectors.toList());
        Planet planet = planetUseCase.getPlanetById(planetId);
        String[] planetSize = planet.getSize().split("");
        int planetXSize = Integer.parseInt(planetSize[0]);
        int planetYSize = Integer.parseInt(planetSize[1]);

        return (point.x > planetXSize || point.y > planetYSize || rocketCoordinatesList.contains(point));
    }
}
