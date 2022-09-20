package com.elo7.junior.dev.challenge.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.awt.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "planet")
@EntityListeners(AuditingEntityListener.class)
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private Point size;
}
