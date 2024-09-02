package com.example.particleacceleratormonitoring.services.alerts;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;

public interface PressureAlertService {
    Page<DataAlert> getAllPressureAlerts(int page, int size);
}
