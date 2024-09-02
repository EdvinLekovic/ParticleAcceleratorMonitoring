package com.example.particleacceleratormonitoring.services.monitors;

import com.example.particleacceleratormonitoring.events.TemperatureEvent;
import com.example.particleacceleratormonitoring.models.alerts.TemperatureAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.models.enums.SensorType;
import com.example.particleacceleratormonitoring.repositories.TemperatureAlertRepository;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TemperatureMonitor {

    private final TemperatureAlertRepository temperatureAlertRepository;
    private final WebSocketService webSocketService;

    public TemperatureMonitor(TemperatureAlertRepository temperatureAlertRepository,
                              WebSocketService webSocketService) {
        this.temperatureAlertRepository = temperatureAlertRepository;
        this.webSocketService = webSocketService;
    }


    @EventListener
    public void onTemperatureEvent(TemperatureEvent event) {
        double minTemperature = event.getTemperature().getTemperatureSensor().getMinTemperature();
        double maxTemperature = event.getTemperature().getTemperatureSensor().getMaxTemperature();
        double temperatureValue = event.getTemperature().getTemperatureValue();
        if (minTemperature > temperatureValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is below the minimum permissible limits.",
                    SensorType.TEMPERATURE, event.getTemperature().getTemperatureSensor().getId());
            TemperatureAlert temperatureAlert = new TemperatureAlert(event.getTemperature().getId(),
                    event.getTemperature().getTemperatureSensor().getId(), errorMessage);
            this.temperatureAlertRepository.save(temperatureAlert);
            this.webSocketService.sendTemperatureAlertUpdate(new DataAlert(temperatureAlert.getTemperatureId(),
                    temperatureAlert.getTemperatureSensorId(),
                    errorMessage,
                    temperatureAlert.getTimestamp()));
        } else if (maxTemperature < temperatureValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is above the maximum permissible limits.",
                    SensorType.TEMPERATURE, event.getTemperature().getTemperatureSensor().getId());
            TemperatureAlert temperatureAlert = new TemperatureAlert(event.getTemperature().getId(),
                    event.getTemperature().getTemperatureSensor().getId(), errorMessage);
            this.temperatureAlertRepository.save(temperatureAlert);
            this.webSocketService.sendTemperatureAlertUpdate(new DataAlert(temperatureAlert.getTemperatureId(),
                    temperatureAlert.getTemperatureSensorId(),
                    errorMessage,
                    temperatureAlert.getTimestamp()));
        }
        System.out.println("Event Listener is called");
    }

}
