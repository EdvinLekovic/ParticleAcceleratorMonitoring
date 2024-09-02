package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.data.Temperature;
import com.example.particleacceleratormonitoring.models.dto.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(t.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(t.temperatureSensor.id, t.temperatureSensor.minTemperature, t.temperatureSensor.maxTemperature, t.temperatureSensor.currentState)," +
            "t.timestamp, t.temperatureValue) " +
            "FROM Temperature t " +
            "ORDER BY t.timestamp DESC")
    Page<Data> findAllByOrderByTimestampDesc(Pageable pageable);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(t.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(t.temperatureSensor.id, t.temperatureSensor.minTemperature, t.temperatureSensor.maxTemperature, t.temperatureSensor.currentState)," +
            "t.timestamp, t.temperatureValue) " +
            "FROM Temperature t " +
            "WHERE t.temperatureSensor.id = :id")
    List<Data> findAllBySensorId(long id);

    @Query("SELECT t.id FROM Temperature t WHERE t.temperatureSensor.id = :id")
    List<Long> findAllTemperatureIdsBySensorId(long id);
}
