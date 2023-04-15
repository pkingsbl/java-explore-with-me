package ru.practicum.dto.compilation;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @NotBlank
    private String title;
}
