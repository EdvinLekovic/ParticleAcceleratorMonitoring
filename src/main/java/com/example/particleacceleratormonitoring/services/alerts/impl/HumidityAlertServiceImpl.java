package com.example.particleacceleratormonitoring.services.alerts.impl;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.repositories.HumidityAlertRepository;
import com.example.particleacceleratormonitoring.services.alerts.HumidityAlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HumidityAlertServiceImpl implements HumidityAlertService {

    private final HumidityAlertRepository humidityAlertRepository;

    public HumidityAlertServiceImpl(HumidityAlertRepository humidityAlertRepository) {
        this.humidityAlertRepository = humidityAlertRepository;
    }


    @Override
    public Page<DataAlert> getAllHumidityAlerts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.humidityAlertRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
