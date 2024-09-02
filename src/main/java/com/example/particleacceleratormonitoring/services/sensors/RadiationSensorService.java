package com.example.particleacceleratormonitoring.services.sensors;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RadiationSensorService {
    boolean schedulingEnabled();

    Optional<Sensor> getRadiationSensor(long sensorId);

    void editRadiationSensor(long sensorId, double minRadiation, double maxRadiation, State state);

    void createRadiationSensor(double minRadiation, double maxRadiation, State state);

    void deleteRadiationSensor(long sensorId);

    Page<Data> getAllRadiations(int page, int size);

    List<Data> getAllRadiationsBySensorId(Long sensorId);

    void startRadiationScheduler(long duration, TimeUnit timeUnit);

    void stopRadiationScheduler();

    List<Sensor> getAllRadiationSensors();
}
