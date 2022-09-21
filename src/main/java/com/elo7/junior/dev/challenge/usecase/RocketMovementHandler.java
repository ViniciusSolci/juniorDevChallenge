package com.elo7.junior.dev.challenge.usecase;

import com.elo7.junior.dev.challenge.entity.Rocket;
import com.elo7.junior.dev.challenge.framework.exception.UnacceptableRocketMovementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;

@RequiredArgsConstructor
@Service
public class RocketMovementHandler {
    private final @NonNull RocketMovementValidator rocketMovementValidator;

    void turnRocketRight(Rocket rocket) {
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

    void turnRocketLeft(Rocket rocket) {
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

    void moveRocketForward(Rocket rocket) {
        Point point = new Point();
        switch (rocket.getFacingDirection()) {
            case NORTH:
                point.setLocation(rocket.getCoordinates().getX() + 1, rocket.getCoordinates().getY());
                break;
            case SOUTH:
                point.setLocation(rocket.getCoordinates().getX() - 1, rocket.getCoordinates().getY());
                break;
            case EAST:
                point.setLocation(rocket.getCoordinates().getX(), rocket.getCoordinates().getY() + 1);
                break;
            case WEST:
                point.setLocation(rocket.getCoordinates().getX() + 1, rocket.getCoordinates().getY() - 1);
                break;
            default:
                break;
        }
        if (!rocketMovementValidator.validatedMovement(rocket.getAllocatedPlanetId(), point)) {
            rocket.setCoordinates(point);
        } else
            throw new UnacceptableRocketMovementException(HttpStatus.CONFLICT, "Rocket movement is blocked by another rocket or planet boundaries");
    }
}
