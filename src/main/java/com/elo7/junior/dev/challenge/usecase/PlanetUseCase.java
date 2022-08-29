package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.PlanetDTO;
import com.elo7.junior.dev.challenge.repository.PlanetRepository;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlanetUseCase {
    @Autowired
    RocketRepository rocketRepository;

    @Autowired
    PlanetRepository planetRepository;

    public Planet createPlanet(PlanetDTO planetDTO) {
        Planet planet = new Planet();
        planet.setName(planetDTO.getName());
        planet.setSize(planetDTO.getSize());

        return planetRepository.save(planet);
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

    public String deletePlanet(long planetId) throws Exception {
        if (planetId == 0) throw new Exception(); //TODO: define better exception

        List<Rocket> rocketList = getRocketsInPlanet(planetId);
        if (rocketList.isEmpty()) {
            planetRepository.deleteById(planetId);
            return "Planet deleted";
        } else throw new Exception(); //TODO: define better exception
    }
}
