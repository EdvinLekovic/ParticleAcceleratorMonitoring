package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.sensors.PressureSensor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PressureSensorEvent extends ApplicationEvent {

    private final PressureSensor pressureSensor;

    public PressureSensorEvent(Object source, PressureSensor pressureSensor) {
        super(source);
        this.pressureSensor = pressureSensor;
    }
}
