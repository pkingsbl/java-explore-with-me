package ru.practicum.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder(toBuilder = true)
public class StatsDto {

    private String app;

    private String uri;

    private Integer hits;

}
