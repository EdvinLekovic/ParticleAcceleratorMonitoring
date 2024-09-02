package com.example.particleacceleratormonitoring.events;

import com.example.particleacceleratormonitoring.models.sensors.HumiditySensor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HumiditySensorEvent extends ApplicationEvent {

    private final HumiditySensor humiditySensor;

    public HumiditySensorEvent(Object source, HumiditySensor humiditySensor) {
        super(source);
        this.humiditySensor = humiditySensor;
    }
}
