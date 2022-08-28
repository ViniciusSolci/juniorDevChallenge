package com.elo7.junior.dev.challenge.framework;

import com.elo7.junior.dev.challenge.usecase.PlanetUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/planet/v1")
public class PlanetResource {

    PlanetUseCase planetUseCase;

}
