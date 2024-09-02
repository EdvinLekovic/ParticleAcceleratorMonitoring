package com.example.particleacceleratormonitoring.models.sensors;

import com.example.particleacceleratormonitoring.models.enums.State;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PressureSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double minPressure;
    private double maxPressure;
    @Enumerated(value = EnumType.STRING)
    private State currentState;

    public PressureSensor(double minPressure, double maxPressure, State currentState) {
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
        this.currentState = currentState;
    }

    public PressureSensor() {

    }
}
