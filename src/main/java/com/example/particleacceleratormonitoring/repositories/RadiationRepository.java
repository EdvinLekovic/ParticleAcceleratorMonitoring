package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.data.Radiation;
import com.example.particleacceleratormonitoring.models.dto.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadiationRepository extends JpaRepository<Radiation, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(r.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "r.radiationSensor.id, " +
            "r.radiationSensor.minRadiation, " +
            "r.radiationSensor.maxRadiation, " +
            "r.radiationSensor.currentState)," +
            "r.timestamp, r.radiationValue) " +
            "FROM Radiation r " +
            "ORDER BY r.timestamp DESC")
    Page<Data> findAllByOrderByTimestampDesc(Pageable pageable);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(r.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "r.radiationSensor.id, " +
            "r.radiationSensor.minRadiation, " +
            "r.radiationSensor.maxRadiation, " +
            "r.radiationSensor.currentState)," +
            "r.timestamp, " +
            "r.radiationValue) " +
            "FROM Radiation r " +
            "WHERE r.radiationSensor.id = :id")
    List<Data> findAllBySensorId(long id);

    @Query("SELECT r.id FROM Radiation r WHERE r.radiationSensor.id = :id")
    List<Long> findAllRadiationIdsBySensorId(long id);
}
