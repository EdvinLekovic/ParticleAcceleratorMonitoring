package com.example.particleacceleratormonitoring.models.sensors;

import com.example.particleacceleratormonitoring.models.enums.State;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HumiditySensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double minHumidity;
    private double maxHumidity;
    @Enumerated(value = EnumType.STRING)
    private State currentState;

    public HumiditySensor(double minHumidity, double maxHumidity, State currentState) {
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.currentState = currentState;
    }

    public HumiditySensor() {

    }
}
