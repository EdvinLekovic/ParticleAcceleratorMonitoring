package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.sensors.HumiditySensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HumiditySensorRepository extends JpaRepository<HumiditySensor, Long> {

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "h.id, " +
            "h.minHumidity, " +
            "h.maxHumidity, " +
            "h.currentState) " +
            "FROM HumiditySensor h " +
            "WHERE h.id = :id")
    Optional<Sensor> findSensorById(long id);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "h.id, " +
            "h.minHumidity, " +
            "h.maxHumidity, " +
            "h.currentState) " +
            "FROM HumiditySensor h")
    List<Sensor> findAllSensors();

    @Query("SELECT h FROM HumiditySensor h WHERE h.currentState = 'MEASURING'")
    List<HumiditySensor> getAllHumiditySensorsInMeasuringState();

    @Query("SELECT COUNT(h.id) FROM HumiditySensor h WHERE h.currentState = 'MEASURING'")
    Long getAllHumiditySensorsInMeasuringStateCount();
}
