package com.example.particleacceleratormonitoring.models.dto;

import com.example.particleacceleratormonitoring.models.enums.State;
import lombok.Getter;

@Getter
public class Sensor {
    private final Long id;
    private final double minValue;
    private final double maxValue;
    private final State currentState;

    public Sensor(Long id, double minValue, double maxValue, State currentState) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentState = currentState;
    }
}
