package com.example.particleacceleratormonitoring.services;

import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.DataAlert;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendTemperatureUpdate(Data data){
        System.out.println("New Temperature is added: " + data);
        this.messagingTemplate.convertAndSend("/topic/temperature", data);
    }

    public void sendPressureUpdate(Data data){
        System.out.println("New Pressure is added: " + data);
        this.messagingTemplate.convertAndSend("/topic/pressure", data);
    }

    public void sendHumidityUpdate(Data data){
        System.out.println("New Humidity is added: " + data);
        this.messagingTemplate.convertAndSend("/topic/humidity", data);
    }

    public void sendRadiationUpdate(Data data){
        System.out.println("New Radiation is added: " + data);
        this.messagingTemplate.convertAndSend("/topic/radiation", data);
    }

    public void sendTemperatureAlertUpdate(DataAlert dataAlert){
        System.out.println("New Temperature Alert is added: " + dataAlert);
        this.messagingTemplate.convertAndSend("/topic/temperature-alert", dataAlert);
    }

    public void sendPressureAlertUpdate(DataAlert dataAlert){
        System.out.println("New Pressure Alert is added: " + dataAlert);
        this.messagingTemplate.convertAndSend("/topic/pressure-alert", dataAlert);
    }

    public void sendHumidityAlertUpdate(DataAlert dataAlert){
        System.out.println("New Humidity Alert is added: " + dataAlert);
        this.messagingTemplate.convertAndSend("/topic/humidity-alert", dataAlert);
    }

    public void sendRadiationAlertUpdate(DataAlert dataAlert){
        System.out.println("New Radiation Alert is added: " + dataAlert);
        this.messagingTemplate.convertAndSend("/topic/radiation-alert", dataAlert);
    }
}
