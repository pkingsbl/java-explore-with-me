package ru.practicum.mapper;

import ru.practicum.dto.event.EventShortDto;
import ru.practicum.entity.Event;

public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(null)
                .category(null)
                .confirmedRequests(null)
                .eventDate(null)
                .initiator(null)
                .paid(null)
                .title(null)
                .views(null)
                .build();
    }


}
