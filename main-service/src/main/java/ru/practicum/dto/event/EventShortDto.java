package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.dto.category.CategoryDto;

@Data
@Builder
public class EventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private String eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;

}
