package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.PlanetDTO;
import com.elo7.junior.dev.challenge.framework.exception.ForbiddenPlanetDestructionException;
import com.elo7.junior.dev.challenge.framework.exception.NotFoundException;
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
public class PlanetUseCase {
    private static final String INVALID_PLANET = "Invalid planet id (planet id < 0)";

    private final @NonNull RocketRepository rocketRepository;

    private final @NonNull PlanetRepository planetRepository;

    public Planet createPlanet(PlanetDTO planetDTO) {
        Planet planet = new Planet();
        planet.setName(planetDTO.getName());
        Point planetSize = new Point(planetDTO.getWidth(), planetDTO.getLength());
        planet.setSize(planetSize);
        return planetRepository.save(planet);
    }

    public Slice<Planet> getAllPlanets(int pageNumber, int pageSize) {
        Pageable sortedById = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return planetRepository.findAll(sortedById);
    }

    public Slice<Planet> getAllPlanets() {
        return planetRepository.findAll(Pageable.unpaged());
    }

    public Planet getPlanetById(long planetId) {
        if (planetId < 0) {
            throw new InvalidParameterException(INVALID_PLANET);
        }
        try {
            return planetRepository.findById(planetId).orElseThrow();
        } catch (NoSuchElementException exception) {
            throw new NotFoundException("Planet with id: " + planetId + " does not exists");
        }
    }

    public Slice<Rocket> getRocketsInPlanet(long planetId, int pageNumber, int pageSize) {
        if (planetId < 0) {
            throw new InvalidParameterException(INVALID_PLANET);
        }
        Pageable sortedById = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return rocketRepository.findByAllocatedPlanetId(sortedById, planetId);
    }

    public Slice<Rocket> getRocketsInPlanet(long planetId) {
        if (planetId < 0) {
            throw new InvalidParameterException(INVALID_PLANET);
        }
        return rocketRepository.findByAllocatedPlanetId(Pageable.unpaged(), planetId);
    }

    public void deletePlanet(long planetId) {
        if (planetId == 0)
            throw new ForbiddenPlanetDestructionException(HttpStatus.UNPROCESSABLE_ENTITY, "Home planet cannot be destroyed");
        if (planetId < 0) throw new InvalidParameterException(INVALID_PLANET);

        if (!rocketRepository.existsByAllocatedPlanetId(planetId)) {
            try {
                planetRepository.deleteById(planetId);
            } catch (IllegalArgumentException exception) {
                throw new NotFoundException("Planet with id: " + planetId + " does not exists");
            }
        } else
            throw new ForbiddenPlanetDestructionException(HttpStatus.UNPROCESSABLE_ENTITY, "Planets with allocated rockets cannot be destroyed");
    }
}
