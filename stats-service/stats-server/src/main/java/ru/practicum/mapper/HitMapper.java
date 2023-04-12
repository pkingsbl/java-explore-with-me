package ru.practicum.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.entity.App;
import ru.practicum.entity.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    public static Hit mapToHit(HitDto hitDto, App app) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Hit.builder()
                .app(app)
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), dtf))
                .build();
    }

    public static StatsDto toStatsDto(Hit hit) {
        return StatsDto
                .builder()
                .app(hit.getApp().getApp())
                .uri(hit.getUri())
                .build();
    }

}
