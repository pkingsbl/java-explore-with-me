package ru.practicum.service;

import ru.practicum.dto.GetStatsRequest;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

public interface StatsService {

    void postHit(HitDto hitDto);

    List<StatsDto> getStats(GetStatsRequest statsRequest);

}
