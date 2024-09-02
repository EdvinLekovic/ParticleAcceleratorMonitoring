package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.data.Humidity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HumidityEvent extends ApplicationEvent {

    private final Humidity humidity;

    public HumidityEvent(Object source, Humidity humidity) {
        super(source);
        this.humidity = humidity;
    }
}
