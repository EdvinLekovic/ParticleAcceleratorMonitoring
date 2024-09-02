package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.sensors.TemperatureSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemperatureSensorRepository extends JpaRepository<TemperatureSensor, Long> {

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(t.id, t.minTemperature, t.maxTemperature, t.currentState) " +
            "FROM TemperatureSensor t " +
            "WHERE t.id = :id")
    Optional<Sensor> findSensorById(long id);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(t.id, t.minTemperature, t.maxTemperature, t.currentState) " +
            "FROM TemperatureSensor t")
    List<Sensor> findAllSensors();

    @Query("SELECT t FROM TemperatureSensor t WHERE t.currentState = 'MEASURING'")
    List<TemperatureSensor> getAllTemperatureSensorsInMeasuringState();

    @Query("SELECT COUNT(t.id) FROM TemperatureSensor t WHERE t.currentState = 'MEASURING'")
    Long getAllTemperatureSensorsInMeasuringStateCount();
}
