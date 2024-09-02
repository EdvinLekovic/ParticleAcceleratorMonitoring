package com.example.particleacceleratormonitoring.controllers;


import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.services.sensors.HumiditySensorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/humidities")
public class HumidityController {

    private final HumiditySensorService humiditySensorService;

    public HumidityController(HumiditySensorService humiditySensorService) {
        this.humiditySensorService = humiditySensorService;
    }

    @GetMapping
    public String getHumidities(Model model) {
        model.addAttribute("bodyContent", "dashboard-data");
        model.addAttribute("dataTypeName","Humidity");
        model.addAttribute("dataSocketApiUrl", "/topic/humidity");
        model.addAttribute("dataSocketAlertsApiUrl", "/topic/humidity-alert");
        model.addAttribute("dataApiUrl", "/api/humidities");
        model.addAttribute("dataAlertApiUrl", "/api/humidities-alerts");
        return "master-template";
    }

    @GetMapping("/sensors")
    public String getHumiditySensor(Model model) {
        List<Sensor> sensorList = this.humiditySensorService.getAllHumiditySensors();
        List<State> states = List.of(State.ON, State.OFF, State.IDLE, State.MEASURING);
        model.addAttribute("bodyContent", "sensors");
        model.addAttribute("sensorList", sensorList);
        model.addAttribute("states", states);
        model.addAttribute("dataTypeName", "Humidity");
        model.addAttribute("sensorCreationApi", "/humidities/create-humidity-sensor");
        model.addAttribute("sensorEditApi", "/humidities/edit-humidity-sensor/{sensorId}");
        model.addAttribute("sensorDeleteApi", "/humidities/delete-humidity-sensor/{sensorId}");
        return "master-template";
    }

    @GetMapping("/sensors/{sensorId}")
    public String getHumiditySensor(Model model, @PathVariable long sensorId) {
        List<Data> dataList = this.humiditySensorService.getAllHumiditiesBySensorId(sensorId);
        model.addAttribute("bodyContent", "sensor");
        model.addAttribute("dataList", dataList);

        return "master-template";
    }

    @PostMapping("/create-humidity-sensor")
    public String createHumiditySensor(@RequestParam double minValue,
                                          @RequestParam double maxValue,
                                          @RequestParam State state) {
        this.humiditySensorService.createHumiditySensor(minValue, maxValue, state);
        return "redirect:/humidities/sensors";
    }

    @GetMapping("/edit-humidity-sensor/{sensorId}")
    public String editHumiditySensorPage(@PathVariable long sensorId,
                                            Model model) {
        this.humiditySensorService.getHumiditySensor(sensorId).ifPresent(sensor -> {
            model.addAttribute("bodyContent", "sensor-edit-form");
            model.addAttribute("editSensorApi","/humidities/edit-humidity-sensor/");
            model.addAttribute("dataTypeName", "Humidity");
            model.addAttribute("sensor", sensor);
            model.addAttribute("states", List.of(State.ON, State.IDLE, State.MEASURING));
        });
        return "master-template";
    }

    @PostMapping("/edit-humidity-sensor/{sensorId}")
    public String editHumiditySensor(@PathVariable long sensorId,
                                        @RequestParam double minValue,
                                        @RequestParam double maxValue,
                                        @RequestParam State state) {
        this.humiditySensorService.editHumiditySensor(sensorId, minValue, maxValue, state);
        return "redirect:/humidities/sensors";
    }

    @PostMapping("/delete-humidity-sensor/{sensorId}")
    public String deleteHumiditySensor(@PathVariable long sensorId) {
        this.humiditySensorService.deleteHumiditySensor(sensorId);
        return "redirect:/humidities/sensors";
    }

    @PostMapping("/start-scheduler")
    public String startScheduler(@RequestParam long duration,
                                 @RequestParam TimeUnit timeUnit,
                                 HttpServletRequest request) {
        this.humiditySensorService.startHumidityScheduler(duration, timeUnit);
        request.getSession().setAttribute("scheduling", true);
        return "redirect:/scheduler";
    }

    @GetMapping("/stop-scheduler")
    public String stopScheduler() {
        this.humiditySensorService.stopHumidityScheduler();
        return "redirect:/scheduler";
    }

}
