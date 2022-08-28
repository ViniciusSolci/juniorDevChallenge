package com.elo7.junior.dev.challenge.repository;

import com.elo7.junior.dev.challenge.entity.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RocketRepository extends JpaRepository<Rocket, Long> {

}