package com.example.particleacceleratormonitoring.services.monitors;

import com.example.particleacceleratormonitoring.events.RadiationEvent;
import com.example.particleacceleratormonitoring.models.alerts.RadiationAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.models.enums.SensorType;
import com.example.particleacceleratormonitoring.repositories.RadiationAlertRepository;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class RadiationMonitor {

    private final RadiationAlertRepository radiationAlertRepository;
    private final WebSocketService webSocketService;

    public RadiationMonitor(RadiationAlertRepository radiationAlertRepository, WebSocketService webSocketService) {
        this.radiationAlertRepository = radiationAlertRepository;
        this.webSocketService = webSocketService;
    }

    @EventListener
    public void onRadiationEvent(RadiationEvent event) {
        double minRadiation = event.getRadiation().getRadiationSensor().getMinRadiation();
        double maxRadiation = event.getRadiation().getRadiationSensor().getMaxRadiation();
        double radiationValue = event.getRadiation().getRadiationValue();
        if (minRadiation > radiationValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is below the minimum permissible limits.",
                    SensorType.RADIATION, event.getRadiation().getRadiationSensor().getId());
            RadiationAlert radiationAlert = new RadiationAlert(event.getRadiation().getId(),
                    event.getRadiation().getRadiationSensor().getId(),
                    errorMessage);
            this.radiationAlertRepository.save(radiationAlert);
            this.webSocketService.sendRadiationAlertUpdate(new DataAlert(radiationAlert.getRadiationId(),
                    radiationAlert.getRadiationSensorId(),
                    errorMessage,
                    radiationAlert.getTimestamp()));
        } else if (maxRadiation < radiationValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is above the maximum permissible limits.",
                    SensorType.RADIATION,
                    event.getRadiation().getRadiationSensor().getId());
            RadiationAlert radiationAlert = new RadiationAlert(event.getRadiation().getId(),
                    event.getRadiation().getRadiationSensor().getId(),
                    errorMessage);
            this.radiationAlertRepository.save(radiationAlert);
            this.webSocketService.sendRadiationAlertUpdate(new DataAlert(radiationAlert.getRadiationId(),
                    radiationAlert.getRadiationSensorId(),
                    errorMessage,
                    radiationAlert.getTimestamp()));
        }
        System.out.println("Event Listener is called");
    }
}
