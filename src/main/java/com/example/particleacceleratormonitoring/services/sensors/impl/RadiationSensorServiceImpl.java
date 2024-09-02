package com.example.particleacceleratormonitoring.services.sensors.impl;

import com.example.particleacceleratormonitoring.events.RadiationEvent;
import com.example.particleacceleratormonitoring.events.RadiationSensorEvent;
import com.example.particleacceleratormonitoring.models.data.Radiation;
import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.models.sensors.RadiationSensor;
import com.example.particleacceleratormonitoring.repositories.RadiationRepository;
import com.example.particleacceleratormonitoring.repositories.RadiationSensorRepository;
import com.example.particleacceleratormonitoring.services.GenerateNumbersService;
import com.example.particleacceleratormonitoring.services.SchedulerService;
import com.example.particleacceleratormonitoring.services.SettingsService;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import com.example.particleacceleratormonitoring.services.sensors.RadiationSensorService;
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
public class RadiationSensorServiceImpl implements RadiationSensorService {

    private final RadiationRepository radiationRepository;
    private final RadiationSensorRepository radiationSensorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GenerateNumbersService generateNumbersService;
    private final SettingsService settingsService;
    private final SchedulerService schedulerService;
    private final WebSocketService webSocketService;
    private final TaskScheduler taskScheduler;
    private final long radiationScheduleFixedRateInSeconds;
    private ScheduledFuture<?> scheduledTask;

    public RadiationSensorServiceImpl(RadiationRepository radiationRepository,
                                      RadiationSensorRepository radiationSensorRepository,
                                      ApplicationEventPublisher applicationEventPublisher,
                                      GenerateNumbersService generateNumbersService,
                                      SettingsService settingsService, SchedulerService schedulerService, WebSocketService webSocketService,
                                      @Qualifier("radiationTaskScheduler") TaskScheduler taskScheduler,
                                      @Value("${radiation.schedule.rate}") long radiationScheduleFixedRateInSeconds) {
        this.radiationRepository = radiationRepository;
        this.radiationSensorRepository = radiationSensorRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.generateNumbersService = generateNumbersService;
        this.settingsService = settingsService;
        this.schedulerService = schedulerService;
        this.webSocketService = webSocketService;
        this.taskScheduler = taskScheduler;
        this.radiationScheduleFixedRateInSeconds = radiationScheduleFixedRateInSeconds;
    }

    @Override
    public boolean schedulingEnabled() {
        return !(scheduledTask == null || scheduledTask.isCancelled());
    }

    @Override
    public Optional<Sensor> getRadiationSensor(long sensorId) {
        return this.radiationSensorRepository.findSensorById(sensorId);
    }

    @Override
    public void editRadiationSensor(long sensorId, double minRadiation, double maxRadiation, State state) {
        this.radiationSensorRepository.findById(sensorId).ifPresent(radiationSensor -> {
            if (minRadiation != radiationSensor.getMinRadiation()) {
                radiationSensor.setMinRadiation(minRadiation);
            }
            if (maxRadiation != radiationSensor.getMaxRadiation()) {
                radiationSensor.setMaxRadiation(maxRadiation);
            }
            if (!state.equals(radiationSensor.getCurrentState())) {
                radiationSensor.setCurrentState(state);
            }
            radiationSensorRepository.save(radiationSensor);
        });
    }

    @Override
    public void createRadiationSensor(double minRadiation, double maxRadiation, State state) {
        RadiationSensor radiationSensor = new RadiationSensor(minRadiation, maxRadiation, state);
        this.radiationSensorRepository.save(radiationSensor);
        this.applicationEventPublisher.publishEvent(new RadiationSensorEvent(this, radiationSensor));
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            startRadiationScheduler(radiationScheduleFixedRateInSeconds, null);
        }
    }

    @Override
    public void deleteRadiationSensor(long sensorId) {
        this.radiationRepository
                .findAllRadiationIdsBySensorId(sensorId)
                .forEach(this.radiationRepository::deleteById);
        this.radiationSensorRepository.deleteById(sensorId);
    }

    @Override
    public Page<Data> getAllRadiations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.radiationRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    public List<Data> getAllRadiationsBySensorId(Long sensorId) {
        return this.radiationRepository.findAllBySensorId(sensorId);
    }

    @Override
    public void startRadiationScheduler(long duration, TimeUnit timeUnit) {
        long sensorCount = this.radiationSensorRepository.getAllRadiationSensorsInMeasuringStateCount();
        scheduledTask = schedulerService.startScheduler(taskScheduler,
                scheduledTask,
                settingsService,
                generateNumbersService,
                sensorCount,
                duration,
                timeUnit,
                this::getRadiationSensorData);
    }

    @Override
    public void stopRadiationScheduler() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.MANUAL)) {
            scheduledTask.cancel(true);
        } else {
            if (this.radiationSensorRepository.getAllRadiationSensorsInMeasuringStateCount() == 0) {
                scheduledTask.cancel(true);
            }
        }
    }

    @Override
    public List<Sensor> getAllRadiationSensors() {
        return this.radiationSensorRepository.findAllSensors();
    }

    private void getRadiationSensorData() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            stopRadiationScheduler();
        }
        int radiationSensorIndex = generateNumbersService
                .generateRandomIntNumber((int) this.radiationSensorRepository.count());
        RadiationSensor radiationSensor =
                this.radiationSensorRepository
                        .getAllRadiationSensorsInMeasuringState()
                        .get(radiationSensorIndex);
        double radiationValue = generateNumbersService.getNumbersStream();
        Radiation radiation = new Radiation(radiationSensor, radiationValue);
        this.radiationRepository.save(radiation);
        this.applicationEventPublisher.publishEvent(new RadiationEvent(this, radiation));
        this.webSocketService.sendRadiationUpdate(new Data(radiation.getId(),
                new Sensor(radiationSensor.getId(),
                        radiationSensor.getMinRadiation(),
                        radiationSensor.getMaxRadiation(),
                        radiationSensor.getCurrentState()),
                radiation.getTimestamp(),
                radiationValue));
    }
}
