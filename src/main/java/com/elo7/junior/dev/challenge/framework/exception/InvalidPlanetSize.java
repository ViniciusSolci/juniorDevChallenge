package com.elo7.junior.dev.challenge.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPlanetSize extends ResponseStatusException {
    public InvalidPlanetSize(HttpStatus status, String reason) {
        super(status, reason);
    }
}
