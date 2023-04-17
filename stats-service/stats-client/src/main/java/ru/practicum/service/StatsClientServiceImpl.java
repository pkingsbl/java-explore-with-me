package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class StatsClientServiceImpl implements StatsClientService {

    @Value("${stats-server.url}")
    private String serverUrl;

    @Value("${stat-client.name}")
    private String name;
    private final RestTemplate rest;

    @Override
    public void saveHit(String uri, long eventId, String ip) {
        HitDto hitDto = HitDto.builder()
                .app(serverUrl + name)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().toString())
                .build();
        try {
            postHit(hitDto);
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }
    }


    @Override
    public ResponseEntity<Object> postHit(HitDto hitDto) {
        log.info("POST Client Statistic hit for app: {}", hitDto.getApp());

        return rest.exchange(serverUrl + "/hit", HttpMethod.POST, new HttpEntity<>(hitDto), Object.class);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        log.info("GET Client Statistic for from: {} to: {} unique: {} for {}",
                start, end, unique, uris != null ? uris : "all");

        Map<String, Object> parameters = new java.util.HashMap<>(Map.of(
                "start", start,
                "end", end
        ));
        if (uris != null) {
            parameters.put("uris", uris);
        }
        if (unique != null) {
            parameters.put("unique", unique);
        }

        ResponseEntity<StatsDto[]> response = rest
                .exchange(serverUrl + "/stats", HttpMethod.GET, new HttpEntity<>(""), StatsDto[].class, parameters);
        StatsDto[] result = response.getBody();

        return result != null ? Arrays.asList(result) : List.of();
    }

}
