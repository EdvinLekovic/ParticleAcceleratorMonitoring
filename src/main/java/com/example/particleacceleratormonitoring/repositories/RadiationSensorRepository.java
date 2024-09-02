package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.sensors.RadiationSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadiationSensorRepository extends JpaRepository<RadiationSensor, Long> {

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "r.id, " +
            "r.minRadiation, " +
            "r.maxRadiation, " +
            "r.currentState) " +
            "FROM RadiationSensor r " +
            "WHERE r.id = :id")
    Optional<Sensor> findSensorById(long id);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "r.id, " +
            "r.minRadiation, " +
            "r.maxRadiation, " +
            "r.currentState) " +
            "FROM RadiationSensor r")
    List<Sensor> findAllSensors();

    @Query("SELECT r FROM RadiationSensor r WHERE r.currentState = 'MEASURING'")
    List<RadiationSensor> getAllRadiationSensorsInMeasuringState();

    @Query("SELECT COUNT(r.id) FROM RadiationSensor r WHERE r.currentState = 'MEASURING'")
    Long getAllRadiationSensorsInMeasuringStateCount();
}
