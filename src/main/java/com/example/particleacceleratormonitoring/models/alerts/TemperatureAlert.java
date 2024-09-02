package com.example.particleacceleratormonitoring.models.alerts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class TemperatureAlert {
    @Id
    private Long temperatureId;
    private Long temperatureSensorId;
    private String message;
    private LocalDateTime timestamp;

    public TemperatureAlert(Long temperatureId,Long temperatureSensorId, String message) {
        this.temperatureId = temperatureId;
        this.temperatureSensorId = temperatureSensorId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public TemperatureAlert() {
        this.timestamp = LocalDateTime.now();
    }
}
