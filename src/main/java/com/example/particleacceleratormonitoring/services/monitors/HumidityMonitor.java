package com.example.particleacceleratormonitoring.services.monitors;

import com.example.particleacceleratormonitoring.events.HumidityEvent;
import com.example.particleacceleratormonitoring.models.alerts.HumidityAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.models.enums.SensorType;
import com.example.particleacceleratormonitoring.repositories.HumidityAlertRepository;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class HumidityMonitor {

    private final HumidityAlertRepository humidityAlertRepository;
    private final WebSocketService webSocketService;

    public HumidityMonitor(HumidityAlertRepository humidityAlertRepository, WebSocketService webSocketService) {
        this.humidityAlertRepository = humidityAlertRepository;
        this.webSocketService = webSocketService;
    }

    @EventListener
    public void onHumidityEvent(HumidityEvent event) {
        double minHumidity = event.getHumidity().getHumiditySensor().getMinHumidity();
        double maxHumidity = event.getHumidity().getHumiditySensor().getMaxHumidity();
        double radiationValue = event.getHumidity().getHumidityValue();
        if (minHumidity > radiationValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is below the minimum permissible limits.",
                    SensorType.HUMIDITY, event.getHumidity().getHumiditySensor().getId());
            HumidityAlert humidityAlert = new HumidityAlert(
                    event.getHumidity().getId(),
                    event.getHumidity().getHumiditySensor().getId(),
                    errorMessage);
            this.humidityAlertRepository.save(humidityAlert);
            this.webSocketService.sendHumidityAlertUpdate(new DataAlert(humidityAlert.getHumidityId(),
                    humidityAlert.getHumiditySensorId(),
                    errorMessage,
                    humidityAlert.getTimestamp()));
        } else if (maxHumidity < radiationValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is above the maximum permissible limits.",
                    SensorType.HUMIDITY, event.getHumidity().getHumiditySensor().getId());
            HumidityAlert humidityAlert = new HumidityAlert(
                    event.getHumidity().getId(),
                    event.getHumidity().getHumiditySensor().getId(),
                    errorMessage);
            this.humidityAlertRepository.save(humidityAlert);
            this.webSocketService.sendHumidityAlertUpdate(new DataAlert(humidityAlert.getHumidityId(),
                    humidityAlert.getHumiditySensorId(),
                    errorMessage,
                    humidityAlert.getTimestamp()));
        }
        System.out.println("Event Listener is called");
    }
}
