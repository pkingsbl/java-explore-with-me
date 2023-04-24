package ru.practicum.dto.compilation;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateCompilationRequest {

    private List<Long> events;

    private Boolean pinned;

    private String title;

}
