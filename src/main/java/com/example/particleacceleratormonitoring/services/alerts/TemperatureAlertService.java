package com.example.particleacceleratormonitoring.services.alerts;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;

public interface TemperatureAlertService {
    Page<DataAlert> getAllTemperatureAlerts(int page, int size);
}
