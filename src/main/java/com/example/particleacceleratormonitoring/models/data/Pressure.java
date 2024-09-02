package com.example.particleacceleratormonitoring.models.data;

import com.example.particleacceleratormonitoring.models.sensors.PressureSensor;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Pressure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PressureSensor pressureSensor;
    private LocalDateTime timestamp;
    private double pressureValue;

    public Pressure(PressureSensor pressureSensor, double pressureValue) {
        this.pressureSensor = pressureSensor;
        this.pressureValue = pressureValue;
        this.timestamp = LocalDateTime.now();
    }

    public Pressure() {
        this.timestamp = LocalDateTime.now();
    }
}
