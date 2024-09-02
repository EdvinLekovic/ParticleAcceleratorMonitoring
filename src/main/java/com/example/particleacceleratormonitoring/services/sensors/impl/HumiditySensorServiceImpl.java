package com.example.particleacceleratormonitoring.services.sensors.impl;

import com.example.particleacceleratormonitoring.events.HumidityEvent;
import com.example.particleacceleratormonitoring.events.HumiditySensorEvent;
import com.example.particleacceleratormonitoring.models.data.Humidity;
import com.example.particleacceleratormonitoring.models.dto.Data;
import com.example.particleacceleratormonitoring.models.dto.Sensor;
import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import com.example.particleacceleratormonitoring.models.enums.State;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import com.example.particleacceleratormonitoring.models.sensors.HumiditySensor;
import com.example.particleacceleratormonitoring.repositories.HumidityRepository;
import com.example.particleacceleratormonitoring.repositories.HumiditySensorRepository;
import com.example.particleacceleratormonitoring.services.GenerateNumbersService;
import com.example.particleacceleratormonitoring.services.SchedulerService;
import com.example.particleacceleratormonitoring.services.SettingsService;
import com.example.particleacceleratormonitoring.services.WebSocketService;
import com.example.particleacceleratormonitoring.services.sensors.HumiditySensorService;
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
public class HumiditySensorServiceImpl implements HumiditySensorService {

    private final HumidityRepository humidityRepository;
    private final HumiditySensorRepository humiditySensorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GenerateNumbersService generateNumbersService;
    private final SettingsService settingsService;
    private final SchedulerService schedulerService;
    private final WebSocketService webSocketService;
    private final TaskScheduler taskScheduler;
    private final long humidityScheduleFixedRateInSeconds;
    private ScheduledFuture<?> scheduledTask;

    public HumiditySensorServiceImpl(HumidityRepository humidityRepository,
                                     HumiditySensorRepository humiditySensorRepository,
                                     ApplicationEventPublisher applicationEventPublisher,
                                     GenerateNumbersService generateNumbersService,
                                     SettingsService settingsService, SchedulerService schedulerService, WebSocketService webSocketService,
                                     @Qualifier("humidityTaskScheduler") TaskScheduler taskScheduler,
                                     @Value("${humidity.schedule.rate}") long humidityScheduleFixedRateInSeconds) {
        this.humidityRepository = humidityRepository;
        this.humiditySensorRepository = humiditySensorRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.generateNumbersService = generateNumbersService;
        this.settingsService = settingsService;
        this.schedulerService = schedulerService;
        this.webSocketService = webSocketService;
        this.taskScheduler = taskScheduler;
        this.humidityScheduleFixedRateInSeconds = humidityScheduleFixedRateInSeconds;
    }

    @Override
    public boolean schedulingEnabled() {
        return !(scheduledTask == null || scheduledTask.isCancelled());
    }


    @Override
    public Optional<Sensor> getHumiditySensor(long sensorId) {
        return this.humiditySensorRepository.findSensorById(sensorId);
    }

    @Override
    public void editHumiditySensor(long sensorId, double minHumidity, double maxHumidity, State state) {
        this.humiditySensorRepository.findById(sensorId).ifPresent(humiditySensor -> {
            if (minHumidity != humiditySensor.getMinHumidity()) {
                humiditySensor.setMinHumidity(minHumidity);
            }
            if (maxHumidity != humiditySensor.getMaxHumidity()) {
                humiditySensor.setMaxHumidity(maxHumidity);
            }
            if (state.equals(humiditySensor.getCurrentState())) {
                humiditySensor.setCurrentState(state);
            }
            this.humiditySensorRepository.save(humiditySensor);
        });
    }

    @Override
    public void createHumiditySensor(double minHumidity, double maxHumidity, State state) {
        HumiditySensor humiditySensor = new HumiditySensor(minHumidity, maxHumidity, state);
        this.humiditySensorRepository.save(humiditySensor);
        this.applicationEventPublisher.publishEvent(new HumiditySensorEvent(this, humiditySensor));
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            startHumidityScheduler(humidityScheduleFixedRateInSeconds, null);
        }
    }

    @Override
    public void deleteHumiditySensor(long sensorId) {
        this.humidityRepository
                .findAllHumidityIdsBySensorId(sensorId)
                .forEach(this.humidityRepository::deleteById);
        this.humiditySensorRepository.deleteById(sensorId);
    }

    @Override
    public Page<Data> getAllHumidities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.humidityRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    public List<Data> getAllHumiditiesBySensorId(Long sensorId) {
        return this.humidityRepository.findAllBySensorId(sensorId);
    }

    @Override
    public void startHumidityScheduler(long duration, TimeUnit timeUnit) {
       long sensorCount = this.humiditySensorRepository.getAllHumiditySensorsInMeasuringStateCount();
        scheduledTask = schedulerService.startScheduler(taskScheduler,
                scheduledTask,
                settingsService,
                generateNumbersService,
                sensorCount,
                duration,
                timeUnit,
                this::getHumiditySensorData);
    }

    @Override
    public void stopHumidityScheduler() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.MANUAL)) {
            scheduledTask.cancel(true);
        } else {
            if (this.humiditySensorRepository.getAllHumiditySensorsInMeasuringStateCount() == 0) {
                scheduledTask.cancel(true);
            }
        }
    }

    @Override
    public List<Sensor> getAllHumiditySensors() {
        return this.humiditySensorRepository.findAllSensors();
    }

    private void getHumiditySensorData() {
        if (settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.AUTO)) {
            stopHumidityScheduler();
        }
        int humiditySensorIndex = generateNumbersService
                .generateRandomIntNumber((int) this.humiditySensorRepository.count());
        HumiditySensor humiditySensor =
                this.humiditySensorRepository
                        .getAllHumiditySensorsInMeasuringState()
                        .get(humiditySensorIndex);
        double humidityValue = generateNumbersService.getNumbersStream();
        Humidity humidity = new Humidity(humiditySensor, humidityValue);
        this.humidityRepository.save(humidity);
        this.applicationEventPublisher.publishEvent(new HumidityEvent(this, humidity));
        this.webSocketService.sendHumidityUpdate(new Data(humidity.getId(),
                new Sensor(humiditySensor.getId(),
                        humiditySensor.getMinHumidity(),
                        humiditySensor.getMaxHumidity(),
                        humiditySensor.getCurrentState()),
                humidity.getTimestamp(),
                humidityValue));
    }
}
