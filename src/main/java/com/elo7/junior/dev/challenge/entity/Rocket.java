package com.elo7.junior.dev.challenge.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "rocket")
@EntityListeners(AuditingEntityListener.class)
public class Rocket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "allocated_planet_id")
    private long allocatedPlanetId;

    @Column(name = "facing_direction")
    private String facingDirection;

    @Column(name = "coordinates")
    private String coordinates;

    private Integer xCoordinate;

    private Integer yCoordinate;
}
