package com.example.particleacceleratormonitoring.controllers.rest;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.services.alerts.HumidityAlertService;
import com.example.particleacceleratormonitoring.services.sensors.HumiditySensorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HumidityRestController {

    private final HumiditySensorService humiditySensorService;
    private final HumidityAlertService humidityAlertService;

    public HumidityRestController(HumiditySensorService humiditySensorService, HumidityAlertService humidityAlertService) {
        this.humiditySensorService = humiditySensorService;
        this.humidityAlertService = humidityAlertService;
    }

    @GetMapping("/api/humidities")
    public Page<Data> getPressures(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        return this.humiditySensorService.getAllHumidities(page, size);
    }

    @GetMapping("/api/humidities-alerts")
    public Page<DataAlert> getPressureAlerts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size){
        return this.humidityAlertService.getAllHumidityAlerts(page, size);
    }
}
