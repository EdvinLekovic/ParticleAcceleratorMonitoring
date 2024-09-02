package com.example.particleacceleratormonitoring.controllers.rest;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.services.alerts.TemperatureAlertService;
import com.example.particleacceleratormonitoring.services.sensors.TemperatureSensorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureRestController {

    private final TemperatureSensorService temperatureSensorService;
    private final TemperatureAlertService temperatureAlertService;

    public TemperatureRestController(TemperatureSensorService temperatureSensorService,
                                     TemperatureAlertService temperatureAlertService) {
        this.temperatureSensorService = temperatureSensorService;
        this.temperatureAlertService = temperatureAlertService;
    }

    @GetMapping("/api/temperatures")
    public Page<Data> getTemperatures(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        return this.temperatureSensorService.getAllTemperatures(page, size);
    }

    @GetMapping("/api/temperatures-alerts")
    public Page<DataAlert> getTemperatureAlerts(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size){
        return this.temperatureAlertService.getAllTemperatureAlerts(page, size);
    }
}
