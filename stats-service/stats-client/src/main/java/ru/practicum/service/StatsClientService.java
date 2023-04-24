package ru.practicum.service;

import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StatsClientService {

    void post(HttpServletRequest request);

    Long getStats(String uris, Boolean unique);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

}
