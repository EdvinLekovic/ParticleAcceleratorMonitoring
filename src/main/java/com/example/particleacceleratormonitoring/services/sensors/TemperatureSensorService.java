package com.example.particleacceleratormonitoring.services.sensors;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TemperatureSensorService {
    boolean schedulingEnabled();

    Optional<Sensor> getTemperatureSensor(long sensorId);

    void editTemperatureSensor(long sensorId, double minTemperature, double maxTemperature, State state);

    void createTemperatureSensor(double minTemperature, double maxTemperature, State state);

    void deleteTemperatureSensor(long sensorId);

    Page<Data> getAllTemperatures(int page, int size);

    List<Data> getAllTemperaturesBySensorId(long sensorId);

    void startTemperatureScheduler(long duration, TimeUnit timeUnit);

    void stopTemperatureScheduler();

    List<Sensor> getAllTemperatureSensors();
}
