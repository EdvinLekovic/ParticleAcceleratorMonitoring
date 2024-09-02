package com.example.particleacceleratormonitoring.models.data;

import com.example.particleacceleratormonitoring.models.sensors.RadiationSensor;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Radiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private RadiationSensor radiationSensor;
    private LocalDateTime timestamp;
    private double radiationValue;

    public Radiation(RadiationSensor radiationSensor, double radiationValue) {
        this.radiationSensor = radiationSensor;
        this.radiationValue = radiationValue;
        this.timestamp = LocalDateTime.now();
    }


    public Radiation() {
        this.timestamp = LocalDateTime.now();
    }


}
