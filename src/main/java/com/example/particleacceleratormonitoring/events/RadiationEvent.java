package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.data.Radiation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RadiationEvent extends ApplicationEvent {

    private final Radiation radiation;

    public RadiationEvent(Object source, Radiation radiation) {
        super(source);
        this.radiation = radiation;
    }
}
