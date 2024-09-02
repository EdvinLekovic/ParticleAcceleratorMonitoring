package com.example.particleacceleratormonitoring.controllers.rest;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.services.alerts.RadiationAlertService;
import com.example.particleacceleratormonitoring.services.sensors.RadiationSensorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RadiationRestController {

    private final RadiationSensorService radiationSensorService;
    private final RadiationAlertService radiationAlertService;

    public RadiationRestController(RadiationSensorService radiationSensorService, RadiationAlertService radiationAlertService) {
        this.radiationSensorService = radiationSensorService;
        this.radiationAlertService = radiationAlertService;
    }

    @GetMapping("/api/radiations")
    public Page<Data> getPressures(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        return this.radiationSensorService.getAllRadiations(page, size);
    }

    @GetMapping("/api/radiations-alerts")
    public Page<DataAlert> getPressureAlerts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size){
        return this.radiationAlertService.getAllRadiationAlerts(page, size);
    }
}
