package ru.practicum.service.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.StatusEnum;
import ru.practicum.entity.Event;
import ru.practicum.entity.Participation;
import ru.practicum.entity.User;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.event.StateEventFullEnum.*;
import static ru.practicum.dto.request.StatusEnum.CONFIRMED;
import static ru.practicum.mapper.ParticipationMapper.toParticipationRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info("User id: {}. Get user requests", userId);

        findUser(userId);
        return participationRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        log.info("User id: {}. Add participation request event id: {}", userId, eventId);

        User user = findUser(userId);
        Event event = findEvent(eventId);
        if (participationRepository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ConflictException("Participation request already exist");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Requester can`t be initiator of event");
        }
        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        if (event.getParticipantLimit() <= participationRepository
                .countByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new ConflictException("The limit of requests for participation has been exhausted");
        }
        Participation participation = Participation
                .builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(StatusEnum.PENDING)
                .build();

        return toParticipationRequestDto(participationRepository.saveAndFlush(participation));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("User id: {}. Cancel request id: {}", userId, requestId);
        findUser(userId);
        Participation participation = participationRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " and requestId " + requestId + " was not found"));
        participation.setStatus(StatusEnum.CANCELED);

        return toParticipationRequestDto(participationRepository.save(participation));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

}
