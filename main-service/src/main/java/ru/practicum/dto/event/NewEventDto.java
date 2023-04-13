package ru.practicum.dto.event;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class NewEventDto {

    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    private Long category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @NotBlank
    private String eventDate;

    @NotBlank
    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;

}
