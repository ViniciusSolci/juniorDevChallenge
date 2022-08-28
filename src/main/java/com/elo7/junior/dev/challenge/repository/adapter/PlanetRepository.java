package com.elo7.junior.dev.challenge.repository.adapter;

import com.elo7.junior.dev.challenge.entity.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {

}
