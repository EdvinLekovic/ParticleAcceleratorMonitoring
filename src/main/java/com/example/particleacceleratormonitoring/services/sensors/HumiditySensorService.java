package com.example.particleacceleratormonitoring.services.sensors;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HumiditySensorService {
    boolean schedulingEnabled();

    Optional<Sensor> getHumiditySensor(long sensorId);

    void editHumiditySensor(long sensorId, double minHumidity, double maxHumidity, State state);

    void createHumiditySensor(double minHumidity, double maxHumidity, State state);

    void deleteHumiditySensor(long sensorId);

    Page<Data> getAllHumidities(int page, int size);

    List<Data> getAllHumiditiesBySensorId(Long sensorId);

    void startHumidityScheduler(long duration, TimeUnit timeUnit);

    void stopHumidityScheduler();

    List<Sensor> getAllHumiditySensors();
}
