package com.example.particleacceleratormonitoring.controllers;


import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.services.sensors.PressureSensorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pressures")
public class PressureController {

    private final PressureSensorService pressureSensorService;

    public PressureController(PressureSensorService pressureSensorService) {
        this.pressureSensorService = pressureSensorService;
    }

    @GetMapping
    public String getPressures(Model model) {
        model.addAttribute("bodyContent", "dashboard-data");
        model.addAttribute("dataTypeName","Pressure");
        model.addAttribute("dataSocketApiUrl", "/topic/pressure");
        model.addAttribute("dataSocketAlertsApiUrl", "/topic/pressure-alert");
        model.addAttribute("dataApiUrl", "/api/pressures");
        model.addAttribute("dataAlertApiUrl", "/api/pressures-alerts");
        return "master-template";
    }

    @GetMapping("/sensors")
    public String getPressureSensors(Model model) {
        List<Sensor> sensorList = this.pressureSensorService.getAllPressureSensors();
        List<State> states = List.of(State.ON, State.OFF, State.IDLE, State.MEASURING);
        model.addAttribute("bodyContent", "sensors");
        model.addAttribute("sensorList", sensorList);
        model.addAttribute("states", states);
        model.addAttribute("dataTypeName", "Pressure");
        model.addAttribute("sensorCreationApi", "/pressures/create-pressure-sensor");
        model.addAttribute("sensorEditApi", "/pressures/edit-pressure-sensor/{sensorId}");
        model.addAttribute("sensorDeleteApi", "/pressures/delete-pressure-sensor/{sensorId}");
        return "master-template";
    }

    @GetMapping("/sensors/{sensorId}")
    public String getPressureSensor(Model model, @PathVariable long sensorId) {
        List<Data> dataList = this.pressureSensorService.getAllPressuresBySensorId(sensorId);
        model.addAttribute("bodyContent", "sensor");
        model.addAttribute("dataList", dataList);

        return "master-template";
    }

    @PostMapping("/create-pressure-sensor")
    public String createPressureSensor(@RequestParam double minValue,
                                          @RequestParam double maxValue,
                                          @RequestParam State state) {
        this.pressureSensorService.createPressureSensor(minValue, maxValue, state);
        return "redirect:/pressures/sensors";
    }

    @GetMapping("/edit-pressure-sensor/{sensorId}")
    public String editPressureSensorPage(@PathVariable long sensorId,
                                            Model model) {
        this.pressureSensorService.getPressureSensor(sensorId).ifPresent(sensor -> {
            model.addAttribute("bodyContent", "sensor-edit-form");
            model.addAttribute("editSensorApi","/pressures/edit-pressure-sensor/");
            model.addAttribute("dataTypeName", "Pressure");
            model.addAttribute("sensor", sensor);
            model.addAttribute("states", List.of(State.ON, State.IDLE, State.MEASURING));
        });
        return "master-template";
    }

    @PostMapping("/edit-pressure-sensor/{sensorId}")
    public String editPressureSensor(@PathVariable long sensorId,
                                        @RequestParam double minValue,
                                        @RequestParam double maxValue,
                                        @RequestParam State state) {
        this.pressureSensorService.editPressureSensor(sensorId, minValue, maxValue, state);
        return "redirect:/pressures/sensors";
    }

    @PostMapping("/delete-pressure-sensor/{sensorId}")
    public String deletePressureSensor(@PathVariable long sensorId) {
        this.pressureSensorService.deletePressureSensor(sensorId);
        return "redirect:/pressures/sensors";
    }

    @PostMapping("/start-scheduler")
    public String startScheduler(@RequestParam long duration,
                                 @RequestParam TimeUnit timeUnit,
                                 HttpServletRequest request) {
        this.pressureSensorService.startPressureScheduler(duration, timeUnit);
        request.getSession().setAttribute("scheduling", true);
        return "redirect:/scheduler";
    }

    @GetMapping("/stop-scheduler")
    public String stopScheduler() {
        this.pressureSensorService.stopPressureScheduler();
        return "redirect:/scheduler";
    }

}
