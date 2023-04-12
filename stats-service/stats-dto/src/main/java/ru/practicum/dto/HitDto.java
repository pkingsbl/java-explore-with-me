package ru.practicum.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class HitDto {

    private Long id;

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    private String timestamp;

}
