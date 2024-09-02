package com.example.particleacceleratormonitoring.models.alerts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class HumidityAlert {
    @Id
    private Long humidityId;
    private Long humiditySensorId;
    private String message;
    private LocalDateTime timestamp;

    public HumidityAlert(Long humidityId,Long humiditySensorId, String message) {
        this.humidityId = humidityId;
        this.humiditySensorId = humiditySensorId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public HumidityAlert() {
        this.timestamp = LocalDateTime.now();
    }
}
