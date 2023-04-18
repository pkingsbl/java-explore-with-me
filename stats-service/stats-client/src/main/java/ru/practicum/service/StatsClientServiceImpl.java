package ru.practicum.service;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class StatsClientServiceImpl implements StatsClientService {

    @Value("${stats-server.url}")
    private String serverUrl;

    @Value("${client-server.name}")
    private String name;
    private final RestTemplate rest;

    @Override
    public void post(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        log.info("POST Client Statistic for uri: {}, ip: {}", uri, ip);

        HitDto hitDto = HitDto.builder()
                .app(name)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        rest.exchange(serverUrl + "/hit", HttpMethod.POST, new HttpEntity<>(hitDto), Object.class);
    }

    @Override
    public Long getStats(String uris, Boolean unique) {
        log.info("GET Client Statistic unique: {} for {}", unique, uris != null ? uris : "all");

        List<StatsDto> statsDtos = getStats("1923-01-01 00:00:00",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                List.of(uris), unique);

        return statsDtos.size() > 0 ? statsDtos.get(0).getHits() : 0L;
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
        return Unirest.get(serverUrl + "/stats")
                .queryString("start", start)
                .queryString("end", end)
                .queryString("uris", uris)
                .queryString("unique", unique)
                .asObject(new GenericType<List<StatsDto>>() {
                })
                .getBody();
    }
}
