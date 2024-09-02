package com.example.particleacceleratormonitoring.services.alerts;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.data.domain.Page;

public interface RadiationAlertService {
    Page<DataAlert> getAllRadiationAlerts(int page, int size);
}
