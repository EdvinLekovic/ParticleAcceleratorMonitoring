package com.example.particleacceleratormonitoring.services.alerts.impl;

import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.repositories.RadiationAlertRepository;
import com.example.particleacceleratormonitoring.services.alerts.RadiationAlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RadiationAlertServiceImpl implements RadiationAlertService {

    private final RadiationAlertRepository radiationAlertRepository;

    public RadiationAlertServiceImpl(RadiationAlertRepository radiationAlertRepository) {
        this.radiationAlertRepository = radiationAlertRepository;
    }


    @Override
    public Page<DataAlert> getAllRadiationAlerts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.radiationAlertRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
