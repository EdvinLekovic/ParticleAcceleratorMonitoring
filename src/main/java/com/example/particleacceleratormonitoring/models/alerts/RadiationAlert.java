package com.example.particleacceleratormonitoring.models.alerts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class RadiationAlert {
    @Id
    private Long radiationId;
    private Long radiationSensorId;
    private String message;
    private LocalDateTime timestamp;

    public RadiationAlert(Long radiationId,Long radiationSensorId, String message) {
        this.radiationId = radiationId;
        this.radiationSensorId = radiationSensorId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public RadiationAlert() {
        this.timestamp = LocalDateTime.now();
    }
}
