package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface EventService {

    List<EventFullDto> getEvents(AdminGetEventsRequest adminGetEventsRequest, String requestURI);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest, String requestURI);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventShortDto> getEvents(PublicGetEventsRequest publicGetEventsRequest, String requestURI);

    EventFullDto getEvent(Long id, String requestURI);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
