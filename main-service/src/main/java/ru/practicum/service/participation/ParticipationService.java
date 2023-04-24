package ru.practicum.service.participation;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {


    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

}
