package com.example.particleacceleratormonitoring.models.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class Data {
    private final Long id;
    private final Sensor sensor;
    private final LocalDateTime timestamp;
    private final double value;

    public Data(Long id, Sensor sensor, LocalDateTime timestamp, double value) {
        this.id = id;
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.value = value;
    }

}
