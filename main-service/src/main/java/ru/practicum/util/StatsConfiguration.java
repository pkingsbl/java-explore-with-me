package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.service.StatsClientServiceImpl;

@Component
@RequiredArgsConstructor
public class StatsConfiguration {

    @Bean
    public StatsClientServiceImpl statsClientService() {
        return new StatsClientServiceImpl(new RestTemplate());
    }
}
