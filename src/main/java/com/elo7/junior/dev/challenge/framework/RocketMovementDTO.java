package com.elo7.junior.dev.challenge.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class RocketMovementDTO {
    @NotBlank(message = "Movement list is mandatory")
    @JsonProperty("movementList")
    String movementList;
}
