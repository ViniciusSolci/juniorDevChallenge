package com.elo7.junior.dev.challenge.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.awt.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "rocket")
@EntityListeners(AuditingEntityListener.class)
public class Rocket {

    public Rocket() {
        this.setCoordinates(new Point(0, 0));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "allocated_planet_id")
    private long allocatedPlanetId;

    @Column(name = "facing_direction")
    private FACING_DIRECTION facingDirection;

    @Column(name = "x_coordinate")
    private int xCoordinate;

    @Column(name = "y_coordinate")
    private int yCoordinate;

    public Point getPointCoordinates() {
        return new Point(xCoordinate, yCoordinate);
    }

    public void setCoordinates(Point coordinates) {
        this.xCoordinate = coordinates.x;
        this.yCoordinate = coordinates.y;
    }

    public enum FACING_DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
