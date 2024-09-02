package com.example.particleacceleratormonitoring.controllers;

import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.services.sensors.HumiditySensorService;
import com.example.particleacceleratormonitoring.services.sensors.PressureSensorService;
import com.example.particleacceleratormonitoring.services.sensors.RadiationSensorService;
import com.example.particleacceleratormonitoring.services.sensors.TemperatureSensorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController {

    private final TemperatureSensorService temperatureSensorService;
    private final HumiditySensorService humiditySensorService;
    private final PressureSensorService pressureSensorService;
    private final RadiationSensorService radiationSensorService;

    public SchedulerController(TemperatureSensorService temperatureSensorService, HumiditySensorService humiditySensorService, PressureSensorService pressureSensorService, RadiationSensorService radiationSensorService) {
        this.temperatureSensorService = temperatureSensorService;
        this.humiditySensorService = humiditySensorService;
        this.pressureSensorService = pressureSensorService;
        this.radiationSensorService = radiationSensorService;
    }

    @GetMapping
    public String scheduler(Model model) {
        boolean temperatureSchedulerExist = temperatureSensorService.schedulingEnabled();
        boolean humiditySchedulerExist = humiditySensorService.schedulingEnabled();
        boolean pressureSchedulerExist = pressureSensorService.schedulingEnabled();
        boolean radiationSchedulerExist = radiationSensorService.schedulingEnabled();
        model.addAttribute("bodyContent", "scheduler");
        model.addAttribute("temperatureSchedulerExist", temperatureSchedulerExist);
        model.addAttribute("humiditySchedulerExist", humiditySchedulerExist);
        model.addAttribute("pressureSchedulerExist", pressureSchedulerExist);
        model.addAttribute("radiationSchedulerExist", radiationSchedulerExist);
        model.addAttribute("timeUnits",
                List.of(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS));
        return "master-template";
    }
}
