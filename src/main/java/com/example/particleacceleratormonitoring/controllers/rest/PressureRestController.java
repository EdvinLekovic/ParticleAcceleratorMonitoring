package com.example.particleacceleratormonitoring.controllers.rest;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.services.alerts.PressureAlertService;
import com.example.particleacceleratormonitoring.services.sensors.PressureSensorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PressureRestController {

    private final PressureSensorService pressureSensorService;
    private final PressureAlertService pressureAlertService;

    public PressureRestController(PressureSensorService pressureSensorService, PressureAlertService pressureAlertService) {
        this.pressureSensorService = pressureSensorService;
        this.pressureAlertService = pressureAlertService;
    }

    @GetMapping("/api/pressures")
    public Page<Data> getPressures(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        return this.pressureSensorService.getAllPressures(page, size);
    }

    @GetMapping("/api/pressures-alerts")
    public Page<DataAlert> getPressureAlerts(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size){
        return this.pressureAlertService.getAllPressureAlerts(page, size);
    }
}
