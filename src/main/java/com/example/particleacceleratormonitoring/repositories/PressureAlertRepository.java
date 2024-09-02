package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.alerts.PressureAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PressureAlertRepository extends JpaRepository<PressureAlert, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.DataAlert(pa.pressureId, pa.pressureSensorId, pa.message, pa.timestamp) " +
            "FROM PressureAlert pa " +
            "ORDER BY pa.timestamp DESC")
    Page<DataAlert> findAllByOrderByTimestampDesc(Pageable pageable);
}
