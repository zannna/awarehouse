package com.example.awarehouse.util.configuration;

import com.example.awarehouse.util.TimeSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
class TimeConfiguration {

    @Bean
    TimeSupplier getTimeProvider() {
        return ZonedDateTime::now;
    }
}
