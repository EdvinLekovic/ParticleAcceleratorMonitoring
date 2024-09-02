package com.example.particleacceleratormonitoring.exceptions;

import com.example.particleacceleratormonitoring.models.enums.SensorType;

public class SensorValuesAboveMaxAlertException extends RuntimeException {
    public SensorValuesAboveMaxAlertException(SensorType sensorType, Long sensorId) {
        super(String.format("The %s sensor with id %d register value that is above the maximum permissible limits.", sensorType, sensorId));
    }
}
