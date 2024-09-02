package com.example.particleacceleratormonitoring.exceptions;

import com.example.particleacceleratormonitoring.models.enums.SensorType;

public class SensorValuesBelowMinAlertException extends RuntimeException {
    public SensorValuesBelowMinAlertException(SensorType sensorType, Long sensorId) {
        super(String.format("The %s sensor with id %d register value is below the minimum permissible limits.", sensorType, sensorId));
    }
}
