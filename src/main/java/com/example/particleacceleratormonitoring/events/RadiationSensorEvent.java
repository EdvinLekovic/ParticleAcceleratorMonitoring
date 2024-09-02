package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.sensors.RadiationSensor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RadiationSensorEvent extends ApplicationEvent {

    private final RadiationSensor radiationSensor;

    public RadiationSensorEvent(Object source, RadiationSensor radiationSensor) {
        super(source);
        this.radiationSensor = radiationSensor;
    }
}
