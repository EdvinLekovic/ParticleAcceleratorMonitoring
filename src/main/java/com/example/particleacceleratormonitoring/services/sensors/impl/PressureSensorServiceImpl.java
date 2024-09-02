package com.example.particleacceleratormonitoring.services.sensors.impl;

import com.example.particleacceleratormonitoring.events.PressureEvent;
import com.example.particleacceleratormonitoring.events.PressureSensorEvent;
import com.example.particleacceleratormonitoring.models.data.Pressure;
import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.models.sensors.PressureSensor;
import com.example.particleacceleratormonitoring.repositories.PressureRepository;
import com.example.particleacceleratormonitoring.repositories.PressureSensorRepository;
import com.example.particleacceleratormonitoring.services.GenerateNumbersService;
import com.example.particleacceleratormonitoring.services.SchedulerService;
import com.example.particleacceleratormonitoring.services.SettingsService;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import com.example.particleacceleratormonitoring.services.sensors.PressureSensorService;
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
public class PressureSensorServiceImpl implements PressureSensorService {

    private final PressureRepository pressureRepository;
    private final PressureSensorRepository pressureSensorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GenerateNumbersService generateNumbersService;
    private final SettingsService settingsService;
    private final SchedulerService schedulerService;
    private final WebSocketService webSocketService;
    private final TaskScheduler taskScheduler;
    private final long pressureScheduleFixedRateInSeconds;
    private ScheduledFuture<?> scheduledTask;

    public PressureSensorServiceImpl(PressureRepository pressureRepository,
                                     PressureSensorRepository pressureSensorRepository,
                                     ApplicationEventPublisher applicationEventPublisher,
                                     GenerateNumbersService generateNumbersService,
                                     SettingsService settingsService, SchedulerService schedulerService, WebSocketService webSocketService,
                                     @Qualifier("pressureTaskScheduler") TaskScheduler taskScheduler,
                                     @Value("${pressure.schedule.rate}") long pressureScheduleFixedRateInSeconds) {
        this.pressureRepository = pressureRepository;
        this.pressureSensorRepository = pressureSensorRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.generateNumbersService = generateNumbersService;
        this.settingsService = settingsService;
        this.schedulerService = schedulerService;
        this.webSocketService = webSocketService;
        this.taskScheduler = taskScheduler;
        this.pressureScheduleFixedRateInSeconds = pressureScheduleFixedRateInSeconds;
    }

    @Override
    public boolean schedulingEnabled() {
        return !(scheduledTask == null || scheduledTask.isCancelled());
    }

    @Override
    public Optional<Sensor> getPressureSensor(long sensorId) {
        return this.pressureSensorRepository.findSensorById(sensorId);
    }

    @Override
    public void editPressureSensor(long sensorId, double minPressure, double maxPressure, State state) {
        this.pressureSensorRepository.findById(sensorId).ifPresent(pressureSensor -> {
            if (minPressure != pressureSensor.getMinPressure()) {
                pressureSensor.setMinPressure(minPressure);
            }
            if (maxPressure != pressureSensor.getMaxPressure()) {
                pressureSensor.setMaxPressure(maxPressure);
            }
            if (!state.equals(pressureSensor.getCurrentState())) {
                pressureSensor.setCurrentState(state);
            }
            this.pressureSensorRepository.save(pressureSensor);
        });
    }

    @Override
    public void createPressureSensor(double minPressure, double maxPressure, State state) {
        PressureSensor pressureSensor = new PressureSensor(minPressure, maxPressure, state);
        this.pressureSensorRepository.save(pressureSensor);
        this.applicationEventPublisher.publishEvent(new PressureSensorEvent(this, pressureSensor));
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            startPressureScheduler(pressureScheduleFixedRateInSeconds, null);
        }
    }

    @Override
    public void deletePressureSensor(long sensorId) {
        this.pressureRepository
                .findAllPressureIdsBySensorId(sensorId)
                .forEach(this.pressureRepository::deleteById);
        this.pressureSensorRepository.deleteById(sensorId);
    }

    @Override
    public Page<Data> getAllPressures(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.pressureRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    public List<Data> getAllPressuresBySensorId(Long sensorId) {
        return this.pressureRepository.findAllBySensorId(sensorId);
    }

    @Override
    public void startPressureScheduler(long duration, TimeUnit timeUnit) {
        long sensorCount = this.pressureSensorRepository.getAllPressureSensorsInMeasuringStateCount();
        scheduledTask = schedulerService.startScheduler(taskScheduler,
                scheduledTask,
                settingsService,
                generateNumbersService,
                sensorCount,
                duration,
                timeUnit,
                this::getPressureSensorData);
    }

    @Override
    public void stopPressureScheduler() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.MANUAL)) {
            scheduledTask.cancel(true);
        } else {
            if (this.pressureSensorRepository.getAllPressureSensorsInMeasuringStateCount() == 0) {
                scheduledTask.cancel(true);
            }
        }
    }

    @Override
    public List<Sensor> getAllPressureSensors() {
        return this.pressureSensorRepository.findAllSensors();
    }


    void getPressureSensorData() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            stopPressureScheduler();
        }
        int pressureSensorIndex = generateNumbersService
                .generateRandomIntNumber((int) this.pressureSensorRepository.count());
        PressureSensor pressureSensor = this.pressureSensorRepository
                .getAllPressureSensorsInMeasuringState().get(pressureSensorIndex);
        double pressureValue = generateNumbersService.getNumbersStream();
        Pressure pressure = new Pressure(pressureSensor, pressureValue);
        this.pressureRepository.save(pressure);
        this.applicationEventPublisher.publishEvent(new PressureEvent(this, pressure));
        this.webSocketService.sendPressureUpdate(new Data(pressure.getId(),
                new Sensor(pressureSensor.getId(),
                        pressureSensor.getMinPressure(),
                        pressureSensor.getMaxPressure(),
                        pressureSensor.getCurrentState()),
                        pressure.getTimestamp(),
                        pressureValue));
    }


}
