package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.*;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

public class RocketUseCase {
    @Autowired
    private RocketRepository rocketRepository;

    public Rocket createRocket() {
        Rocket rocket = new Rocket();
        rocket.setAllocatedPlanetId(0);

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
                case "m": //TODO: verify if movement is allowed
                    if (movementAllowed(rocket)) {
                        switch (rocket.getFacingDirection().toLowerCase()) {
                            case "n":
                                rocket.setXCoordinate(rocket.getXCoordinate() + 1);
                                break;
                            case "s":
                                rocket.setXCoordinate(rocket.getXCoordinate() - 1);
                                break;
                            case "e":
                                rocket.setYCoordinate(rocket.getYCoordinate() + 1);
                                break;
                            case "w":
                                rocket.setYCoordinate(rocket.getYCoordinate() - 1);
                                break;
                            default:
                                break;
                        }
                    } else throw new UnacceptableRocketMovementException(HttpStatus.NOT_ACCEPTABLE, "Rocket has reached planet boarder and can no longer move in " + rocket.getFacingDirection() +" direction");
                    break;
                case "l":
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
                    break;
                case "r":
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
                    break;
                default:
                    throw new UnacceptableMovementCommandException(HttpStatus.NOT_ACCEPTABLE, "Unacceptable movement command was found in the movement list");
            }
        }

        return rocketRepository.save(rocket);
    }

    public Rocket sendToPlanet(long rocketId, long planetId) {
        Rocket rocket = getRocketById(rocketId);
        if (rocket.getAllocatedPlanetId() == planetId) {
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at destiny planet");
        } else if (rocket.getAllocatedPlanetId() != 0){
            throw new RocketAlreadyAtPlanetException(HttpStatus.PRECONDITION_FAILED, "Rocket already allocated at planet. Rocket must be recalled before sent to a new planet");
        }
        rocket.setAllocatedPlanetId(planetId);//TODO: verify if landing position is available

        return rocket;
    }

    public Rocket recallRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() != 0) {
            rocket.setAllocatedPlanetId(0);
            return rocketRepository.save(rocket);
        } else throw new UnallocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket is not allocated. Only allocated rockets can be recalled");
    }

    public String destroyRocket(long rocketId) {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() == 0) {
            rocketRepository.delete(rocket);
            return "Rocket successfully destroyed";
        } else throw new AllocatedRocketException(HttpStatus.PRECONDITION_FAILED, "Rocket must be recalled before it's destroyed");
    }
}
