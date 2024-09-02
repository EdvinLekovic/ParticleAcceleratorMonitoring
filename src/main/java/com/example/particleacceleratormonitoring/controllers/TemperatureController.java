package com.example.particleacceleratormonitoring.controllers;


import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.services.sensors.TemperatureSensorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/temperatures")
public class TemperatureController {

    private final TemperatureSensorService temperatureSensorService;

    public TemperatureController(TemperatureSensorService temperatureSensorService) {
        this.temperatureSensorService = temperatureSensorService;
    }

    @GetMapping
    public String getTemperatures(Model model) {
        model.addAttribute("bodyContent", "dashboard-data");
        model.addAttribute("dataTypeName","Temperature");
        model.addAttribute("dataSocketApiUrl", "/topic/temperature");
        model.addAttribute("dataSocketAlertsApiUrl", "/topic/temperature-alert");
        model.addAttribute("dataApiUrl", "/api/temperatures");
        model.addAttribute("dataAlertApiUrl", "/api/temperatures-alerts");
        return "master-template";
    }

    @GetMapping("/sensors")
    public String getTemperatureSensors(Model model) {
        List<Sensor> sensorList = this.temperatureSensorService.getAllTemperatureSensors();
        List<State> states = List.of(State.ON, State.OFF, State.IDLE, State.MEASURING);
        model.addAttribute("bodyContent", "sensors");
        model.addAttribute("sensorList", sensorList);
        model.addAttribute("states", states);
        model.addAttribute("dataTypeName", "Temperature");
        model.addAttribute("sensorCreationApi", "/temperatures/create-temperature-sensor");
        model.addAttribute("sensorEditApi", "/temperatures/edit-temperature-sensor/{sensorId}");
        model.addAttribute("sensorDeleteApi", "/temperatures/delete-temperature-sensor/{sensorId}");
        return "master-template";
    }

    @GetMapping("/sensors/{sensorId}")
    public String getTemperatureSensor(Model model, @PathVariable long sensorId) {
        List<Data> dataList = this.temperatureSensorService.getAllTemperaturesBySensorId(sensorId);
        model.addAttribute("bodyContent", "sensor");
        model.addAttribute("dataList", dataList);

        return "master-template";
    }

    @PostMapping("/create-temperature-sensor")
    public String createTemperatureSensor(@RequestParam double minValue,
                                          @RequestParam double maxValue,
                                          @RequestParam State state) {
        this.temperatureSensorService.createTemperatureSensor(minValue, maxValue, state);
        return "redirect:/temperatures/sensors";
    }

    @GetMapping("/edit-temperature-sensor/{sensorId}")
    public String editTemperatureSensorPage(@PathVariable long sensorId,
                                            Model model) {
        this.temperatureSensorService.getTemperatureSensor(sensorId).ifPresent(sensor -> {
            model.addAttribute("bodyContent", "sensor-edit-form");
            model.addAttribute("editSensorApi","/temperatures/edit-temperature-sensor/");
            model.addAttribute("dataTypeName", "Temperature");
            model.addAttribute("sensor", sensor);
            model.addAttribute("states", List.of(State.ON, State.IDLE, State.MEASURING));
        });
        return "master-template";
    }

    @PostMapping("/edit-temperature-sensor/{sensorId}")
    public String editTemperatureSensor(@PathVariable long sensorId,
                                        @RequestParam double minValue,
                                        @RequestParam double maxValue,
                                        @RequestParam State state) {
        this.temperatureSensorService.editTemperatureSensor(sensorId, minValue, maxValue, state);
        return "redirect:/temperatures/sensors";
    }

    @PostMapping("/delete-temperature-sensor/{sensorId}")
    public String deleteTemperatureSensor(@PathVariable long sensorId) {
        this.temperatureSensorService.deleteTemperatureSensor(sensorId);
        return "redirect:/temperatures/sensors";
    }

    @PostMapping("/start-scheduler")
    public String startScheduler(@RequestParam long duration,
                                 @RequestParam TimeUnit timeUnit,
                                 HttpServletRequest request) {
        this.temperatureSensorService.startTemperatureScheduler(duration, timeUnit);
        request.getSession().setAttribute("scheduling", true);
        return "redirect:/scheduler";
    }

    @GetMapping("/stop-scheduler")
    public String stopScheduler() {
        this.temperatureSensorService.stopTemperatureScheduler();
        return "redirect:/scheduler";
    }

}
