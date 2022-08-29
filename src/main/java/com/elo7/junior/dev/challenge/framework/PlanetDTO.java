package com.elo7.junior.dev.challenge.framework;

import lombok.Data;

import javax.persistence.Column;

@Data
public class PlanetDTO {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private String size;
}
