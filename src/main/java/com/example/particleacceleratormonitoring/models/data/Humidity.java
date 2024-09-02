package com.example.particleacceleratormonitoring.models.data;

import com.example.particleacceleratormonitoring.models.sensors.HumiditySensor;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Entity
@Getter
public class Humidity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private HumiditySensor humiditySensor;
    private LocalDateTime timestamp;
    private double humidityValue;

    public Humidity(HumiditySensor humiditySensor, double humidityValue) {
        this.humiditySensor = humiditySensor;
        this.humidityValue = humidityValue;
        this.timestamp = LocalDateTime.now();
    }

    public Humidity() {
        this.timestamp = LocalDateTime.now();
    }
}
