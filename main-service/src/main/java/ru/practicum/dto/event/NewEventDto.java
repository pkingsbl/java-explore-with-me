package ru.practicum.dto.event;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.entity.Location;

import javax.validation.constraints.NotBlank;

@Getter
public class NewEventDto {

    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @NotBlank
    private String eventDate;

    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;

}
