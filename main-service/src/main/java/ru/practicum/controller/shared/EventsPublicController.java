package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.PublicGetEventsRequest;
import ru.practicum.service.StatsClientServiceImpl;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventsPublicController {

    private final EventService eventService;
    private final StatsClientServiceImpl client;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size,
            HttpServletRequest request
    ) {
        List<EventShortDto> eventShortDtos = eventService
                        .getEvents(PublicGetEventsRequest.of(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size),
                                request.getRequestURI());
        client.post(request);
        return new ResponseEntity<>(eventShortDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id, HttpServletRequest request) {
        client.post(request);
        EventFullDto eventFullDto = eventService.getEvent(id, request.getRequestURI());
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

}
