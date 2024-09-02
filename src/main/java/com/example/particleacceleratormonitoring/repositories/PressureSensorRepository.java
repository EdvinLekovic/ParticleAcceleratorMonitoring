package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.sensors.PressureSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PressureSensorRepository extends JpaRepository<PressureSensor, Long> {

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "p.id, " +
            "p.minPressure, " +
            "p.maxPressure, " +
            "p.currentState) " +
            "FROM PressureSensor p " +
            "WHERE p.id = :id")
    Optional<Sensor> findSensorById(long id);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "p.id, " +
            "p.minPressure, " +
            "p.maxPressure, " +
            "p.currentState) " +
            "FROM PressureSensor p")
    List<Sensor> findAllSensors();

    @Query("SELECT p FROM PressureSensor p WHERE p.currentState = 'MEASURING'")
    List<PressureSensor> getAllPressureSensorsInMeasuringState();

    @Query("SELECT COUNT(p.id) FROM PressureSensor p WHERE p.currentState = 'MEASURING'")
    Long getAllPressureSensorsInMeasuringStateCount();
}
