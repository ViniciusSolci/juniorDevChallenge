package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Planet;
import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.NoSpaceLeftForLandingsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RocketMovementValidator {

    private final @NonNull PlanetUseCase planetUseCase;

    public void validatedLandingPosition(Rocket rocket, long planetId) {
        List<Point> rocketCoordinatesList = planetUseCase.getRocketsInPlanet(planetId).stream().map(Rocket::getCoordinates).collect(Collectors.toList());
        Planet planet = planetUseCase.getPlanetById(planetId);
        int planetXSize = planet.getSize().x;
        int planetYSize = planet.getSize().y;

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

    public boolean validatedMovement(long planetId, Point point) {
        List<Point> rocketCoordinatesList = planetUseCase.getRocketsInPlanet(planetId).stream().map(Rocket::getCoordinates).collect(Collectors.toList());
        Planet planet = planetUseCase.getPlanetById(planetId);
        int planetXSize = planet.getSize().x;
        int planetYSize = planet.getSize().y;

        return (point.x < 0 || point.y < 0 || point.x > planetXSize || point.y > planetYSize || rocketCoordinatesList.contains(point));
    }
}
