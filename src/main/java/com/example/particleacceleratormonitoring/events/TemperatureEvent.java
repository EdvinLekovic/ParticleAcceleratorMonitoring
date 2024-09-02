package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.data.Temperature;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TemperatureEvent extends ApplicationEvent {

    private final Temperature temperature;

    public TemperatureEvent(Object source, Temperature temperature) {
        super(source);
        this.temperature = temperature;
    }
}
