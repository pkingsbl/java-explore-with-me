package ru.practicum.dto.compilation;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @NotBlank
    private String title;
}
