package com.example.particleacceleratormonitoring.services.sensors.impl;

import com.example.particleacceleratormonitoring.events.TemperatureEvent;
import com.example.particleacceleratormonitoring.events.TemperatureSensorEvent;
import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.data.Temperature;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.models.sensors.TemperatureSensor;
import com.example.particleacceleratormonitoring.repositories.TemperatureRepository;
import com.example.particleacceleratormonitoring.repositories.TemperatureSensorRepository;
import com.example.particleacceleratormonitoring.services.GenerateNumbersService;
import com.example.particleacceleratormonitoring.services.SchedulerService;
import com.example.particleacceleratormonitoring.services.SettingsService;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import com.example.particleacceleratormonitoring.services.sensors.TemperatureSensorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
public class TemperatureSensorServiceImpl implements TemperatureSensorService {

    private final TemperatureRepository temperatureRepository;
    private final TemperatureSensorRepository temperatureSensorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GenerateNumbersService generateNumbersService;
    private final SettingsService settingsService;
    private final SchedulerService schedulerService;
    private final WebSocketService webSocketService;
    private final TaskScheduler taskScheduler;
    private final long temperatureScheduleFixedRateInSeconds;
    private ScheduledFuture<?> scheduledTask;

    public TemperatureSensorServiceImpl(TemperatureRepository temperatureRepository,
                                        TemperatureSensorRepository temperatureSensorRepository,
                                        ApplicationEventPublisher applicationEventPublisher,
                                        GenerateNumbersService generateNumbersService,
                                        SettingsService settingsService, SchedulerService schedulerService, WebSocketService webSocketService,
                                        @Qualifier("temperatureTaskScheduler") TaskScheduler taskScheduler,
                                        @Value("${temperature.schedule.rate}") long temperatureScheduleFixedRateInSeconds) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureSensorRepository = temperatureSensorRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.generateNumbersService = generateNumbersService;
        this.settingsService = settingsService;
        this.schedulerService = schedulerService;
        this.webSocketService = webSocketService;
        this.taskScheduler = taskScheduler;
        this.temperatureScheduleFixedRateInSeconds = temperatureScheduleFixedRateInSeconds;
    }

    @Override
    public boolean schedulingEnabled() {
        return !(scheduledTask == null || scheduledTask.isCancelled());
    }

    @Override
    public Optional<Sensor> getTemperatureSensor(long sensorId) {
        return this.temperatureSensorRepository.findSensorById(sensorId);
    }

    @Override
    public void editTemperatureSensor(long sensorId, double minTemperature, double maxTemperature, State state) {
        this.temperatureSensorRepository.findById(sensorId).ifPresent(temperatureSensor -> {
            if (minTemperature != temperatureSensor.getMinTemperature()) {
                temperatureSensor.setMinTemperature(minTemperature);
            }
            if (maxTemperature != temperatureSensor.getMaxTemperature()) {
                temperatureSensor.setMaxTemperature(maxTemperature);
            }
            if (!state.equals(temperatureSensor.getCurrentState())) {
                temperatureSensor.setCurrentState(state);
            }
            temperatureSensorRepository.save(temperatureSensor);
        });
    }

    @Override
    public void createTemperatureSensor(double minTemperature, double maxTemperature, State state) {
        TemperatureSensor temperatureSensor = new TemperatureSensor(minTemperature, maxTemperature, state);
        this.temperatureSensorRepository.save(temperatureSensor);
        this.applicationEventPublisher.publishEvent(new TemperatureSensorEvent(this, temperatureSensor));
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            startTemperatureScheduler(temperatureScheduleFixedRateInSeconds, null);
        }
    }

    @Override
    public void deleteTemperatureSensor(long sensorId) {
        this.temperatureRepository
                .findAllTemperatureIdsBySensorId(sensorId)
                .forEach(this.temperatureRepository::deleteById);
        this.temperatureSensorRepository.deleteById(sensorId);
    }

    @Override
    public Page<Data> getAllTemperatures(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.temperatureRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    public List<Data> getAllTemperaturesBySensorId(long sensorId) {
        return this.temperatureRepository.findAllBySensorId(sensorId);
    }

    @Override
    public void startTemperatureScheduler(long duration, TimeUnit timeUnit) {
        long sensorCount = this.temperatureSensorRepository.getAllTemperatureSensorsInMeasuringStateCount();
        scheduledTask = schedulerService.startScheduler(taskScheduler,
                scheduledTask,
                settingsService,
                generateNumbersService,
                sensorCount,
                duration,
                timeUnit,
                this::getTemperatureSensorData);
    }

    @Override
    public void stopTemperatureScheduler() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.MANUAL)) {
            scheduledTask.cancel(true);
        } else {
            if (this.temperatureSensorRepository.getAllTemperatureSensorsInMeasuringStateCount() == 0) {
                scheduledTask.cancel(true);
            }
        }
    }

    @Override
    public List<Sensor> getAllTemperatureSensors() {
        return this.temperatureSensorRepository.findAllSensors();
    }

    private void getTemperatureSensorData() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            stopTemperatureScheduler();
        }
        int temperatureSensorIndex = generateNumbersService
                .generateRandomIntNumber((int) this.temperatureSensorRepository.count());
        TemperatureSensor temperatureSensor =
                this.temperatureSensorRepository
                        .getAllTemperatureSensorsInMeasuringState()
                        .get(temperatureSensorIndex);
        double temperatureValue = generateNumbersService.getNumbersStream();
        Temperature temperature = new Temperature(temperatureSensor, temperatureValue);
        this.temperatureRepository.save(temperature);
        this.applicationEventPublisher.publishEvent(new TemperatureEvent(this, temperature));
        this.webSocketService.sendTemperatureUpdate(new Data(temperature.getId(),
                new Sensor(temperatureSensor.getId(),
                        temperatureSensor.getMinTemperature(),
                        temperatureSensor.getMaxTemperature(),
                        temperatureSensor.getCurrentState()),
                temperature.getTimestamp(),
                temperatureValue));
    }
}
