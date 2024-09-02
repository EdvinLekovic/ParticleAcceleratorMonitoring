package com.example.particleacceleratormonitoring.models.alerts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PressureAlert {
    @Id
    private Long pressureId;
    private Long pressureSensorId;
    private String message;
    private LocalDateTime timestamp;

    public PressureAlert(Long pressureId,Long pressureSensorId, String message) {
        this.pressureId = pressureId;
        this.pressureSensorId = pressureSensorId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public PressureAlert() {
        this.timestamp = LocalDateTime.now();
    }
}
