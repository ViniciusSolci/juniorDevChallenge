package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.PlanetDTO;
import com.elo7.junior.dev.challenge.framework.exception.ForbiddenPlanetDestructionException;
import com.elo7.junior.dev.challenge.repository.PlanetRepository;
import com.elo7.junior.dev.challenge.repository.RocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.util.List;

@RequiredArgsConstructor
public class PlanetUseCase {

    private final @NonNull RocketRepository rocketRepository;

    private final @NonNull PlanetRepository planetRepository;

    public Planet createPlanet(PlanetDTO planetDTO) {
        Planet planet = new Planet();
        planet.setName(planetDTO.getName());

        String[] planetSize = planetDTO.getSize().split("[xX]");
        int planetXSize = Integer.parseInt(planetSize[0]);
        int planetYSize = Integer.parseInt(planetSize[1]);

        planet.setXSize(planetXSize);
        planet.setYSize(planetYSize);
        return planetRepository.save(planet);


    }

    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    public Planet getPlanetById(long planetId) {
        return planetRepository.findById(planetId).orElseThrow();
    }

    public List<Rocket> getRocketsInPlanet(long planetId) {
        return rocketRepository.findByAllocatedPlanetId(planetId);
    }

    public void deletePlanet(long planetId) {
        if (planetId == 0)
            throw new ForbiddenPlanetDestructionException(HttpStatus.UNPROCESSABLE_ENTITY, "Home planet cannot be destroyed");

        List<Rocket> rocketList = getRocketsInPlanet(planetId);
        if (rocketList.isEmpty()) {
            planetRepository.deleteById(planetId);
        } else
            throw new ForbiddenPlanetDestructionException(HttpStatus.UNPROCESSABLE_ENTITY, "Planets with allocated rockets cannot be destroyed");
    }
}
