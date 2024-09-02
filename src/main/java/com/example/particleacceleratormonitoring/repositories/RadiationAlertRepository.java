package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.alerts.RadiationAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RadiationAlertRepository extends JpaRepository<RadiationAlert, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.DataAlert(ra.radiationId, ra.radiationSensorId, ra.message, ra.timestamp) " +
            "FROM RadiationAlert ra " +
            "ORDER BY ra.timestamp DESC")
    Page<DataAlert> findAllByOrderByTimestampDesc(Pageable pageable);
}
