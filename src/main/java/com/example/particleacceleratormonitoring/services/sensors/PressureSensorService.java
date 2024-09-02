package com.example.particleacceleratormonitoring.services.sensors;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PressureSensorService {
    boolean schedulingEnabled();

    Optional<Sensor> getPressureSensor(long sensorId);

    void editPressureSensor(long sensorId, double minPressure, double maxPressure, State state);

    void createPressureSensor(double minPressure, double maxPressure, State state);

    void deletePressureSensor(long sensorId);

    Page<Data> getAllPressures(int page, int size);

    List<Data> getAllPressuresBySensorId(Long sensorId);

    void startPressureScheduler(long duration, TimeUnit timeUnit);

    void stopPressureScheduler();

    List<Sensor> getAllPressureSensors();
}
