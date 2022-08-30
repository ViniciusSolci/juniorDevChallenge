package com.elo7.junior.dev.challenge.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AllocatedRocketException extends ResponseStatusException {
    public AllocatedRocketException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
