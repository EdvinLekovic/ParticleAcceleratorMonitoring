package com.example.particleacceleratormonitoring.repositories;

import com.example.particleacceleratormonitoring.models.data.Humidity;
import com.example.particleacceleratormonitoring.models.dto.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumidityRepository extends JpaRepository<Humidity, Long> {
    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(h.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "h.humiditySensor.id, " +
            "h.humiditySensor.minHumidity, " +
            "h.humiditySensor.maxHumidity, " +
            "h.humiditySensor.currentState)," +
            "h.timestamp, h.humidityValue) " +
            "FROM Humidity h " +
            "ORDER BY h.timestamp DESC")
    Page<Data> findAllByOrderByTimestampDesc(Pageable pageable);

    @Query("SELECT new com.example.particleacceleratormonitoring.models.dto.Data(h.id, " +
            "new com.example.particleacceleratormonitoring.models.dto.Sensor(" +
            "h.humiditySensor.id, " +
            "h.humiditySensor.minHumidity, " +
            "h.humiditySensor.maxHumidity, " +
            "h.humiditySensor.currentState)," +
            "h.timestamp, h.humidityValue) " +
            "FROM Humidity h " +
            "WHERE h.humiditySensor.id = :id")
    List<Data> findAllBySensorId(long id);

    @Query("SELECT h.id FROM Humidity h WHERE h.humiditySensor.id = :id")
    List<Long> findAllHumidityIdsBySensorId(long id);
}
