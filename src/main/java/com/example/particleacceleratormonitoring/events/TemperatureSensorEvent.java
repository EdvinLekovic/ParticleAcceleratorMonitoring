package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.sensors.TemperatureSensor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TemperatureSensorEvent extends ApplicationEvent {

    private final TemperatureSensor temperatureSensor;

    public TemperatureSensorEvent(Object source, TemperatureSensor temperatureSensor) {
        super(source);
        this.temperatureSensor = temperatureSensor;
    }
}
