package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.alerts.HumidityAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HumidityAlertRepository extends JpaRepository<HumidityAlert, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.DataAlert(ha.humidityId, ha.humiditySensorId, ha.message, ha.timestamp) " +
            "FROM HumidityAlert ha " +
            "ORDER BY ha.timestamp DESC")
    Page<DataAlert> findAllByOrderByTimestampDesc(Pageable pageable);
}
