package com.example.particleacceleratormonitoring.models.dto;

import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
public class Settings {

    @Enumerated(EnumType.STRING)
    private final MeasurementProcess measurementProcess;

    public Settings(MeasurementProcess measurementProcess) {
        this.measurementProcess = measurementProcess;
    }
}
