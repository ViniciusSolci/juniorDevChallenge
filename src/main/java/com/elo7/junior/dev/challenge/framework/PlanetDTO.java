package com.elo7.junior.dev.challenge.framework;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class PlanetDTO {
    @NotBlank(message = "Plane name is mandatory")
    String name;

    int length;

    int width;
}
