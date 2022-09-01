package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.PlanetDTO;
import com.elo7.junior.dev.challenge.framework.exception.ForbiddenPlanetDestruction;
import com.elo7.junior.dev.challenge.framework.exception.InvalidPlanetSize;
import com.elo7.junior.dev.challenge.repository.PlanetRepository;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

public class PlanetUseCase {
    @Autowired
    RocketRepository rocketRepository;

    @Autowired
    PlanetRepository planetRepository;

    public Planet createPlanet(PlanetDTO planetDTO) {
        Planet planet = new Planet();
        planet.setName(planetDTO.getName());

        String[] planetSize = planetDTO.getSize().split("x");
        String planetXSize = planetSize[0];
        String planetYSize = planetSize[1];

        if (planetXSize.matches("\\d+") && planetYSize.matches("\\d+")) {
            planet.setSize(planetDTO.getSize());

            return planetRepository.save(planet);
        } else
            throw new InvalidPlanetSize(HttpStatus.NOT_ACCEPTABLE, "Planet size must be in the format \"number\"x\"number\"");


    }

    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    public Planet getPlanetById(long planetId) {
        return planetRepository.getReferenceById(planetId);
    }

    public List<Rocket> getRocketsInPlanet(long planetId) {
        return rocketRepository.findByPlanetId(planetId);
    }

    public String deletePlanet(long planetId) {
        if (planetId == 0)
            throw new ForbiddenPlanetDestruction(HttpStatus.NOT_ACCEPTABLE, "Home planet cannot be destroyed");

        List<Rocket> rocketList = getRocketsInPlanet(planetId);
        if (rocketList.isEmpty()) {
            planetRepository.deleteById(planetId);
            return "Planet deleted";
        } else
            throw new ForbiddenPlanetDestruction(HttpStatus.PRECONDITION_FAILED, "Planets with allocated rockets cannot be destroyed");
    }
}
