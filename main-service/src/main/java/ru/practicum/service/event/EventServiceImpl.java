package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.StatusEnum;
import ru.practicum.entity.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.StatsClientService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.event.StateEventFullEnum.*;
import static ru.practicum.dto.request.StatusEnum.CONFIRMED;
import static ru.practicum.dto.request.StatusEnum.REJECTED;
import static ru.practicum.mapper.EventMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatsClientService statsClientService;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventFullDto> getEvents(AdminGetEventsRequest adminGetEventsRequest, String requestURI) {
        log.info("Admin. Get events");

        Pageable pageable = toPageable(adminGetEventsRequest.getFrom(), adminGetEventsRequest.getSize(), null);

        LocalDateTime start = adminGetEventsRequest
                .getRangeStart() == null ? null : LocalDateTime.parse(adminGetEventsRequest.getRangeStart(), DTF);
        LocalDateTime end = adminGetEventsRequest
                .getRangeEnd() == null ? null : LocalDateTime.parse(adminGetEventsRequest.getRangeEnd(), DTF);
        List<StateEventFullEnum> states = adminGetEventsRequest
                .getStates() == null ? null : adminGetEventsRequest.getStates()
                .stream()
                .map(StateEventFullEnum::valueOf)
                .collect(Collectors.toList());
        List<Event> events = eventRepository.admSearchEvents(adminGetEventsRequest.getUsers(), states,
                adminGetEventsRequest.getCategories(), start, end, pageable);

        return events.stream()
                .map(event -> toEventFullDto(event, findConfirmedRequests(event)))
                .peek(eventFullDto -> eventFullDto.setViews(statsClientService.getStats(requestURI, false)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent,String requestURI) {
        log.info("Admin. Update event id: {}", eventId);

        Event event = findEvent(eventId);
        checkState(event, PUBLISHED);
        checkEventDate(updateEvent.getEventDate());

        event.setAnnotation(updateEvent.getAnnotation() == null ? event.getAnnotation() : updateEvent.getAnnotation());
        event.setCategory(updateEvent.getCategory() == null ? event.getCategory() : findCategory(updateEvent.getCategory()));
        event.setDescription(updateEvent.getDescription() == null ? event.getDescription() : updateEvent.getDescription());
        event.setEventDate(updateEvent.getEventDate() == null ? event.getEventDate() : LocalDateTime.parse(updateEvent.getEventDate(), DTF));
        event.setLocation(updateEvent.getLocation() == null ? event.getLocation() : updateEvent.getLocation());
        event.setPaid(updateEvent.getPaid() == null ? event.getPaid() : updateEvent.getPaid());
        event.setParticipantLimit(updateEvent.getParticipantLimit() == null ? event.getParticipantLimit() : updateEvent.getParticipantLimit());
        event.setRequestModeration(updateEvent.getRequestModeration() == null ? event.getRequestModeration() : updateEvent.getRequestModeration());
        event.setTitle(updateEvent.getTitle() == null ? event.getTitle() : updateEvent.getTitle());
        if (updateEvent.getStateAction() == StateActionEnum.PUBLISH_EVENT) {
            checkState(event, CANCELED);

        }
        event.setState(updateEvent.getStateAction() == StateActionEnum.PUBLISH_EVENT ? PUBLISHED : CANCELED);

        EventFullDto eventFullDto = toEventFullDto(eventRepository.saveAndFlush(event), findConfirmedRequests(event));
        eventFullDto.setViews(statsClientService.getStats(requestURI, false));
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        log.info("User id: {}. Get events from: {}, size: {}", userId, from, size);

        findUser(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, toPageable(from, size, null));
        return events.stream()
                .map(event -> toEventShortDto(event, findConfirmedRequests(event)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        log.info("User id: {}. Add event title: {}", userId, newEventDto.getTitle());

        User user = findUser(userId);
        checkEventDate(newEventDto.getEventDate());
        Category category = findCategory(newEventDto.getCategory());
        Event event = toNewEvent(newEventDto, category, user);

        return toEventFullDto(eventRepository.saveAndFlush(event), findConfirmedRequests(event));
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        log.info("User id: {}. Get event id: {}", userId, eventId);

        findUser(userId);
        Event event = findEvent(eventId);
        return toEventFullDto(event, findConfirmedRequests(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        log.info("User id: {}. Update event id: {}", userId, eventId);

        findUser(userId);
        Event event = findEvent(eventId);
        checkState(event, PUBLISHED);
        checkEventDate(updateEvent.getEventDate());

        event.setAnnotation(updateEvent.getAnnotation() == null ? event.getAnnotation() : updateEvent.getAnnotation());
        event.setCategory(updateEvent.getCategory() == null ? event.getCategory() : findCategory(updateEvent.getCategory()));
        event.setDescription(updateEvent.getDescription() == null ? event.getDescription() : updateEvent.getDescription());
        event.setEventDate(updateEvent.getEventDate() == null ? event.getEventDate() : LocalDateTime.parse(updateEvent.getEventDate(), DTF));
        event.setLocation(updateEvent.getLocation() == null ? event.getLocation() : updateEvent.getLocation());
        event.setPaid(updateEvent.getPaid() == null ? event.getPaid() : updateEvent.getPaid());
        event.setParticipantLimit(updateEvent.getParticipantLimit() == null ? event.getParticipantLimit() : updateEvent.getParticipantLimit());
        event.setRequestModeration(updateEvent.getRequestModeration() == null ? event.getRequestModeration() : updateEvent.getRequestModeration());
        event.setState(updateEvent.getStateAction() == StateEnum.CANCEL_REVIEW ? CANCELED : PENDING);
        event.setTitle(updateEvent.getTitle() == null ? event.getTitle() : updateEvent.getTitle());

        return toEventFullDto(eventRepository.saveAndFlush(event), findConfirmedRequests(event));
    }

    @Override
    public List<EventShortDto> getEvents(PublicGetEventsRequest publicGetEventsRequest, String requestURI) {
        log.info("Public. Get events");

        LocalDateTime start = publicGetEventsRequest
                .getRangeStart() == null ? null : LocalDateTime.parse(publicGetEventsRequest.getRangeStart(), DTF);
        LocalDateTime end = publicGetEventsRequest
                .getRangeEnd() == null ? null : LocalDateTime.parse(publicGetEventsRequest.getRangeEnd(), DTF);

        Pageable pageable = toPageable(publicGetEventsRequest.getFrom(), publicGetEventsRequest.getSize(), publicGetEventsRequest.getSort());
        List<Event> events = eventRepository.pbcSearchEvents(publicGetEventsRequest.getText(),
                publicGetEventsRequest.getCategories(), publicGetEventsRequest.getPaid(), start, end, PUBLISHED, pageable);

        if (publicGetEventsRequest.getOnlyAvailable() != null && publicGetEventsRequest.getOnlyAvailable()) {
            events = events.stream()
                    .filter(event -> findConfirmedRequests(event) < event.getParticipantLimit()).collect(Collectors.toList());
        }
        return events.stream()
                .map(event -> toEventShortDto(event, findConfirmedRequests(event)))
                .peek(eventFullDto -> eventFullDto.setViews(statsClientService.getStats(requestURI, false)))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long id, String requestURI) {
        log.info("Public. Get event id: {}", id);

        Event event = findEvent(id);
        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }
        EventFullDto eventFullDto = toEventFullDto(event, findConfirmedRequests(event));
        eventFullDto.setViews(statsClientService.getStats(requestURI, false));
        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        log.info("User id {}. Get event id: {}", userId, eventId);

        return participationRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                    EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("User id {}. Update status request to {}, event id: {}",
                userId, eventRequestStatusUpdateRequest.getStatus(), eventId);

        List<Participation> allById = participationRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        allById.forEach(participation -> {
            checkState(participation, CONFIRMED);
            if (participation.getEvent().getParticipantLimit() <= participationRepository
                    .countByEventIdAndStatus(eventId, CONFIRMED)) {
                throw new ConflictException("The limit of requests for participation has been exhausted");
            }
            participation.setStatus(eventRequestStatusUpdateRequest.getStatus());
        });
        participationRepository.saveAllAndFlush(allById);
        if (eventRequestStatusUpdateRequest.getStatus() == REJECTED) {
            return EventRequestStatusUpdateResult.builder()
                    .rejectedRequests(allById.stream()
                            .map(ParticipationMapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .build();
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(allById.stream()
                        .map(ParticipationMapper::toParticipationRequestDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private Pageable toPageable(Integer from, Integer size, String sort) {
        Sort sorted = Sort.unsorted();
        if (sort != null) {
            sorted = sort.equals("EVENT_DATE") ? Sort.by("eventDate") : Sort.by("views");
        }
        return PageRequest.of(from / size, size, sorted);
    }

    private Long findConfirmedRequests(Event event) {
        return participationRepository.countByEventIdAndStatus(event.getId(), CONFIRMED);
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private static void checkEventDate(String date) {
        if (date != null && LocalDateTime.parse(date, DTF).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + date);
        }
    }

    private static void checkState(Event event, StateEventFullEnum state) {
        if (event.getState().equals(state)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: " + state.name());
        }
    }

    private void checkState(Participation participation, StatusEnum confirmed) {
        if (participation.getStatus().equals(confirmed)) {
            throw new ConflictException("It's not in the right state: " + confirmed.name());
        }
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

}
