package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.data.Pressure;
import com.example.particleacceleratormonitoring.models.dto.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PressureRepository extends JpaRepository<Pressure, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(p.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "p.pressureSensor.id, " +
            "p.pressureSensor.minPressure, " +
            "p.pressureSensor.maxPressure, " +
            "p.pressureSensor.currentState)," +
            "p.timestamp, p.pressureValue) " +
            "FROM Pressure p " +
            "ORDER BY p.timestamp DESC")
    Page<Data> findAllByOrderByTimestampDesc(Pageable pageable);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(p.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "p.pressureSensor.id, " +
            "p.pressureSensor.minPressure, " +
            "p.pressureSensor.maxPressure, " +
            "p.pressureSensor.currentState)," +
            "p.timestamp, p.pressureValue) " +
            "FROM Pressure p " +
            "WHERE p.pressureSensor.id = :id")
    List<Data> findAllBySensorId(long id);

    @Query("SELECT p.id FROM Pressure p WHERE p.pressureSensor.id = :id")
    List<Long> findAllPressureIdsBySensorId(long id);
}
