package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.alerts.TemperatureAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureAlertRepository extends JpaRepository<TemperatureAlert, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.DataAlert(" +
            "ta.temperatureId, " +
            "ta.temperatureSensorId, " +
            "ta.message, " +
            "ta.timestamp) " +
            "FROM TemperatureAlert ta " +
            "ORDER BY ta.timestamp DESC")
    Page<DataAlert> findAllByOrderByTimestampDesc(Pageable pageable);
}
