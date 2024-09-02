package com.example.particleacceleratormonitoring.services.alerts.impl;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.repositories.TemperatureAlertRepository;
import com.example.particleacceleratormonitoring.services.alerts.TemperatureAlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TemperatureAlertServiceImpl implements TemperatureAlertService {

    private final TemperatureAlertRepository temperatureAlertRepository;

    public TemperatureAlertServiceImpl(TemperatureAlertRepository temperatureAlertRepository) {
        this.temperatureAlertRepository = temperatureAlertRepository;
    }

    @Override
    public Page<DataAlert> getAllTemperatureAlerts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.temperatureAlertRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
