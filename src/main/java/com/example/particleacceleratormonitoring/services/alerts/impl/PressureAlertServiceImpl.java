package com.example.particleacceleratormonitoring.services.alerts.impl;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.repositories.PressureAlertRepository;
import com.example.particleacceleratormonitoring.services.alerts.PressureAlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PressureAlertServiceImpl implements PressureAlertService {

    private final PressureAlertRepository pressureAlertRepository;

    public PressureAlertServiceImpl(PressureAlertRepository pressureAlertRepository) {
        this.pressureAlertRepository = pressureAlertRepository;
    }

    @Override
    public Page<DataAlert> getAllPressureAlerts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.pressureAlertRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
