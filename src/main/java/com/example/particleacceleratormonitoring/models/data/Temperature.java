package com.example.particleacceleratormonitoring.models.data;

import com.example.particleacceleratormonitoring.models.sensors.TemperatureSensor;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TemperatureSensor temperatureSensor;
    private LocalDateTime timestamp;
    private double temperatureValue;

    public Temperature(TemperatureSensor temperatureSensor, double temperatureValue) {
        this.temperatureSensor = temperatureSensor;
        this.temperatureValue = temperatureValue;
        this.timestamp = LocalDateTime.now();
    }

    public Temperature() {
        this.timestamp = LocalDateTime.now();
    }
}
