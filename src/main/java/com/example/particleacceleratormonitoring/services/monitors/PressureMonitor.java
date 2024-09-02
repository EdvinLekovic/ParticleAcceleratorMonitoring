package com.example.particleacceleratormonitoring.services.monitors;

import com.example.particleacceleratormonitoring.events.PressureEvent;
import com.example.particleacceleratormonitoring.models.alerts.PressureAlert;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import com.example.particleacceleratormonitoring.models.enums.SensorType;
import com.example.particleacceleratormonitoring.repositories.PressureAlertRepository;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PressureMonitor {

    private final PressureAlertRepository pressureAlertRepository;
    private final WebSocketService webSocketService;

    public PressureMonitor(PressureAlertRepository pressureAlertRepository,
                           WebSocketService webSocketService) {
        this.pressureAlertRepository = pressureAlertRepository;
        this.webSocketService = webSocketService;
    }

    @EventListener
    public void onPressureEvent(PressureEvent event) {
        double minPressure = event.getPressure().getPressureSensor().getMinPressure();
        double maxPressure = event.getPressure().getPressureSensor().getMaxPressure();
        double pressureValue = event.getPressure().getPressureValue();
        if (minPressure > pressureValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is below the minimum permissible limits.",
                    SensorType.PRESSURE, event.getPressure().getPressureSensor().getId());
            PressureAlert pressureAlert = new PressureAlert(event.getPressure().getId(),
                    event.getPressure().getPressureSensor().getId(),
                    errorMessage);
            this.pressureAlertRepository.save(pressureAlert);
            this.webSocketService.sendPressureAlertUpdate(new DataAlert(pressureAlert.getPressureId(),
                    pressureAlert.getPressureSensorId(),
                    errorMessage,
                    pressureAlert.getTimestamp()));
        } else if (maxPressure < pressureValue) {
            String errorMessage = String.format("The %s sensor with id %d register value that is above the maximum permissible limits.",
                    SensorType.PRESSURE, event.getPressure().getPressureSensor().getId());
            PressureAlert pressureAlert = new PressureAlert(event.getPressure().getId(), event.getPressure().getPressureSensor().getId(), errorMessage);
            this.pressureAlertRepository.save(pressureAlert);
            this.webSocketService.sendPressureAlertUpdate(new DataAlert(pressureAlert.getPressureId(),
                    pressureAlert.getPressureSensorId(),
                    errorMessage,
                    pressureAlert.getTimestamp()));
        }
        System.out.println("Event Listener is called");
    }
}
