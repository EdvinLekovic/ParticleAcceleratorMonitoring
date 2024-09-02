package com.example.particleacceleratormonitoring.models.sensors;

import com.example.particleacceleratormonitoring.models.enums.State;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TemperatureSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double minTemperature;
    private double maxTemperature;
    @Enumerated(value = EnumType.STRING)
    private State currentState;

    public TemperatureSensor(double minTemperature, double maxTemperature, State currentState) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.currentState = currentState;
    }

    public TemperatureSensor() {

    }
}
