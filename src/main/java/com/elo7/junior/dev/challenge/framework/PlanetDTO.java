package com.elo7.junior.dev.challenge.framework;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
public class PlanetDTO {
    private static final String SIZE_PATTERN = "^\\d*([xX])\\d*$";

    @NotBlank(message = "Plane name is mandatory")
    String name;

    @Pattern(regexp = SIZE_PATTERN, message = "Planet size must be in the format \"number\"x\"number\"")
    String size;
}
