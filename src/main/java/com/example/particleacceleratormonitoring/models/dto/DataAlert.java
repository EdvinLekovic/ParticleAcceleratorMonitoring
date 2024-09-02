package com.example.particleacceleratormonitoring.models.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DataAlert {
    private final Long dataId;
    private final Long sensorId;
    private final String message;
    private final LocalDateTime timestamp;

    public DataAlert(Long dataId, Long sensorId, String message, LocalDateTime timestamp) {
        this.dataId = dataId;
        this.sensorId = sensorId;
        this.message = message;
        this.timestamp = timestamp;
    }
}
