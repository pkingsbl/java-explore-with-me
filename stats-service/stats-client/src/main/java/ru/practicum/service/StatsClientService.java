package ru.practicum.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

public interface StatsClientService {

    void saveHit(String uri, long eventId, String ip);

    ResponseEntity<Object> postHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
