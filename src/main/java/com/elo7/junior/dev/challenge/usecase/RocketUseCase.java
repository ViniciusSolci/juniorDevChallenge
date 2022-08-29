package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    public Rocket moveRocketById(long rocketId, String movementList) throws Exception {
        Rocket rocket = getRocketById(rocketId);

        for (String movement : movementList.split("")) {
            switch (movement.toLowerCase()) {
                case "m": //TODO: verify if movement is allowed
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
                    }
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
                    }
                    break;
                default:
                    throw new Exception();//TODO: define better exception
            }
        }

        return rocketRepository.save(rocket);
    }

    public Rocket sendToPlanet(long rocketId, long planetId) throws Exception {
        Rocket rocket = getRocketById(rocketId);
        if (rocket.getAllocatedPlanetId() == planetId) {
            throw new Exception(); //TODO: define better exception
        }
        rocket.setAllocatedPlanetId(planetId);//TODO: verify if landing position is available

        return rocket;
    }

    public Rocket recallRocket(long rocketId) throws Exception {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() != 0) {
            rocket.setAllocatedPlanetId(0);
            return rocketRepository.save(rocket);
        } else throw new Exception(); //TODO: define better exception
    }

    public String destroyRocket(long rocketId) throws Exception {
        Rocket rocket = getRocketById(rocketId);

        if (rocket.getAllocatedPlanetId() == 0) {
            rocketRepository.delete(rocket);
            return "Rocket successfully destroyed";
        } else throw new Exception(); //TODO: define better exception
    }
}
