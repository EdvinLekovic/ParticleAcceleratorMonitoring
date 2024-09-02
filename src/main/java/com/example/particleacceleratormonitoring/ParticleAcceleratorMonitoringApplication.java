package com.example.particleacceleratormonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@SpringBootApplication
public class ParticleAcceleratorMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParticleAcceleratorMonitoringApplication.class, args);
    }

    @Bean(name = "temperatureTaskScheduler")
    public TaskScheduler temperatureTaskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

    @Bean(name = "pressureTaskScheduler")
    public TaskScheduler pressureTaskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

    @Bean(name = "humidityTaskScheduler")
    public TaskScheduler humidityTaskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

    @Bean(name = "radiationTaskScheduler")
    public TaskScheduler radiationTaskScheduler() {
        return new ConcurrentTaskScheduler();
    }



}
