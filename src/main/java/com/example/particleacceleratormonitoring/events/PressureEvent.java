package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.data.Pressure;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PressureEvent extends ApplicationEvent {

    private final Pressure pressure;

    public PressureEvent(Object source, Pressure pressure) {
        super(source);
        this.pressure = pressure;
    }
}
