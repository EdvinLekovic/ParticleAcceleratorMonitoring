package com.example.particleacceleratormonitoring.models.sensors;

import com.example.particleacceleratormonitoring.models.enums.State;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RadiationSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double minRadiation;
    private double maxRadiation;
    @Enumerated(value = EnumType.STRING)
    private State currentState;

    public RadiationSensor(double minRadiation, double maxRadiation, State currentState) {
        this.minRadiation = minRadiation;
        this.maxRadiation = maxRadiation;
        this.currentState = currentState;
    }

    public RadiationSensor() {

    }
}
