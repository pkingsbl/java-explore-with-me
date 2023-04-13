package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private List<Long> events;

    private Boolean pinned;

    private String title;

}