package com.example.particleacceleratormonitoring.services;


import com.example.particleacceleratormonitoring.models.enums.TimeUnit;
import org.springframework.stereotype.Service;

import java.util.PrimitiveIterator;
import java.util.Random;

@Service
public class GenerateNumbersService {

    private final Random random = new Random();
    private final PrimitiveIterator.OfDouble numbersStream =
            new Random(System.currentTimeMillis())
                    .doubles(-500, 1000)
                    .iterator();

    public int generateRandomIntNumber(int max) {
        return random.nextInt(max);
    }

    public Double getNumbersStream() {
        return numbersStream.next();
    }

    public Long convertTimeUnitToSeconds(long duration, TimeUnit timeUnit) {
        if (timeUnit.equals(TimeUnit.MINUTES)) {
            duration = duration * 60;
        } else if (timeUnit.equals(TimeUnit.HOURS)) {
            duration = duration * 3600;
        } else if (timeUnit.equals(TimeUnit.DAYS)) {
            duration = duration * 86400;
        }

        return duration;
    }
}
