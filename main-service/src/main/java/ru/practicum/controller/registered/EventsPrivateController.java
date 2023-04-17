package ru.practicum.controller.registered;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventsPrivateController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size
    ) {
        List<EventShortDto> eventShortDtos = eventService.getEvents(userId, from, size);
        return new ResponseEntity<>(eventShortDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        EventFullDto eventFullDto = eventService.addEvent(userId, newEventDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        EventFullDto eventFullDto = eventService.getEvent(userId, eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) {
        EventFullDto eventFullDto = eventService.updateEvent(userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        List<ParticipationRequestDto> participationRequestDtos = eventService.getEventParticipants(userId, eventId);
        return new ResponseEntity<>(participationRequestDtos, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult =
                eventService.changeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
        return new ResponseEntity<>(eventRequestStatusUpdateResult, HttpStatus.OK);
    }
}
