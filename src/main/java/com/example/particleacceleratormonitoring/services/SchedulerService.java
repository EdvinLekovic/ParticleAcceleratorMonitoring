package com.example.particleacceleratormonitoring.services;

import com.example.particleacceleratormonitoring.models.enums.MeasurementProcess;
import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulerService {

    public ScheduledFuture<?> startScheduler(
            TaskScheduler taskScheduler,
            ScheduledFuture<?> scheduledTask,
            SettingsService settingsService,
            GenerateNumbersService generateNumbersService,
            long sensorCount,
            long duration,
            TimeUnit timeUnit,
            Runnable function){

        if (scheduledTask == null || scheduledTask.isCancelled()) {
            if (sensorCount > 0 && settingsService.getSettingsMeasurementProcess().equals(MeasurementProcess.MANUAL)) {
                duration = generateNumbersService.convertTimeUnitToSeconds(duration, timeUnit);
                scheduledTask = taskScheduler
                        .scheduleAtFixedRate(function, Duration.ofSeconds(duration));
            } else {
                if (sensorCount > 0) {
                    scheduledTask = taskScheduler
                            .scheduleAtFixedRate(function, Duration.ofSeconds(duration));
                }
            }
        }
        return scheduledTask;
    }
}
