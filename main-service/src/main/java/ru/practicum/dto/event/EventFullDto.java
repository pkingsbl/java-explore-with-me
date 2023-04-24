package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.entity.Location;

@Data
@Builder
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit = 0;

    private String publishedOn;

    private Boolean requestModeration = true;

    private StateEventFullEnum state;

    private String title;

    private Long views;

}
