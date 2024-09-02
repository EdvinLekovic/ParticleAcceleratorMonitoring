package com.example.particleacceleratormonitoring.controllers;


import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.services.sensors.RadiationSensorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/radiations")
public class RadiationController {

    private final RadiationSensorService radiationSensorService;

    public RadiationController(RadiationSensorService radiationSensorService) {
        this.radiationSensorService = radiationSensorService;
    }

    @GetMapping
    public String getRadiations(Model model) {
        model.addAttribute("bodyContent", "dashboard-data");
        model.addAttribute("dataTypeName","Radiation");
        model.addAttribute("dataSocketApiUrl", "/topic/radiation");
        model.addAttribute("dataSocketAlertsApiUrl", "/topic/radiation-alert");
        model.addAttribute("dataApiUrl", "/api/radiations");
        model.addAttribute("dataAlertApiUrl", "/api/radiations-alerts");
        return "master-template";
    }

    @GetMapping("/sensors")
    public String getRadiationSensors(Model model) {
        List<Sensor> sensorList = this.radiationSensorService.getAllRadiationSensors();
        List<State> states = List.of(State.ON, State.OFF, State.IDLE, State.MEASURING);
        model.addAttribute("bodyContent", "sensors");
        model.addAttribute("sensorList", sensorList);
        model.addAttribute("states", states);
        model.addAttribute("dataTypeName", "Radiation");
        model.addAttribute("sensorCreationApi", "/radiations/create-radiation-sensor");
        model.addAttribute("sensorEditApi", "/radiations/edit-radiation-sensor/{sensorId}");
        model.addAttribute("sensorDeleteApi", "/radiations/delete-radiation-sensor/{sensorId}");
        return "master-template";
    }

    @GetMapping("/sensors/{sensorId}")
    public String getRadiationSensor(Model model, @PathVariable long sensorId) {
        List<Data> dataList = this.radiationSensorService.getAllRadiationsBySensorId(sensorId);
        model.addAttribute("bodyContent", "sensor");
        model.addAttribute("dataList", dataList);

        return "master-template";
    }

    @PostMapping("/create-radiation-sensor")
    public String createRadiationSensor(@RequestParam double minValue,
                                       @RequestParam double maxValue,
                                       @RequestParam State state) {
        this.radiationSensorService.createRadiationSensor(minValue, maxValue, state);
        return "redirect:/radiations/sensors";
    }

    @GetMapping("/edit-radiation-sensor/{sensorId}")
    public String editRadiationSensorPage(@PathVariable long sensorId,
                                         Model model) {
        this.radiationSensorService.getRadiationSensor(sensorId).ifPresent(sensor -> {
            model.addAttribute("bodyContent", "sensor-edit-form");
            model.addAttribute("editSensorApi","/radiations/edit-radiation-sensor/");
            model.addAttribute("dataTypeName", "Radiation");
            model.addAttribute("sensor", sensor);
            model.addAttribute("states", List.of(State.ON, State.IDLE, State.MEASURING));
        });
        return "master-template";
    }

    @PostMapping("/edit-radiation-sensor/{sensorId}")
    public String editRadiationSensor(@PathVariable long sensorId,
                                     @RequestParam double minValue,
                                     @RequestParam double maxValue,
                                     @RequestParam State state) {
        this.radiationSensorService.editRadiationSensor(sensorId, minValue, maxValue, state);
        return "redirect:/radiations/sensors";
    }

    @PostMapping("/delete-radiation-sensor/{sensorId}")
    public String deletePressureSensor(@PathVariable long sensorId) {
        this.radiationSensorService.deleteRadiationSensor(sensorId);
        return "redirect:/radiations/sensors";
    }

    @PostMapping("/start-scheduler")
    public String startScheduler(@RequestParam long duration,
                                 @RequestParam TimeUnit timeUnit,
                                 HttpServletRequest request) {
        this.radiationSensorService.startRadiationScheduler(duration, timeUnit);
        request.getSession().setAttribute("scheduling", true);
        return "redirect:/scheduler";
    }

    @GetMapping("/stop-scheduler")
    public String stopScheduler() {
        this.radiationSensorService.stopRadiationScheduler();
        return "redirect:/scheduler";
    }

}
