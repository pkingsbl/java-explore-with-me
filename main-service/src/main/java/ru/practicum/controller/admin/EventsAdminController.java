package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.AdminGetEventsRequest;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventsAdminController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size,
            HttpServletRequest request
    ) {
        List<EventFullDto> eventFullDtos = eventService
                .getEvents(AdminGetEventsRequest.of(users, states, categories, rangeStart, rangeEnd, from, size),
                        request.getRequestURI());
        return new ResponseEntity<>(eventFullDtos, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            HttpServletRequest request
    ) {
        EventFullDto eventFullDto = eventService.updateEvent(eventId, updateEventAdminRequest, request.getRequestURI());
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

}
