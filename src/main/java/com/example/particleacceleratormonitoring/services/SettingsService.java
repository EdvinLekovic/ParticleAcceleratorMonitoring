package com.example.particleacceleratormonitoring.services;

import com.example.particleacceleratormonitoring.models.dto.Settings;
import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final Settings settings;

    public SettingsService() {
        this.settings = new Settings(MeasurementProcess.MANUAL);
    }

    public MeasurementProcess getSettingsMeasurementProcess() {
        return settings.getMeasurementProcess();
    }
}
