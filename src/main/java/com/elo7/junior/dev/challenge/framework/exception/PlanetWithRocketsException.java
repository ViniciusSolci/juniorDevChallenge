package com.elo7.junior.dev.challenge.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PlanetWithRocketsException extends ResponseStatusException {
    public PlanetWithRocketsException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
