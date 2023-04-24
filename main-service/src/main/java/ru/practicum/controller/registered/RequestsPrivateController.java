package ru.practicum.controller.registered;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.participation.ParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestsPrivateController {

    private final ParticipationService participationService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(@PathVariable Long userId) {
        List<ParticipationRequestDto> participationRequestDtos = participationService.getUserRequests(userId);
        return new ResponseEntity<>(participationRequestDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        ParticipationRequestDto participationRequestDto = participationService.addParticipationRequest(userId, eventId);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        ParticipationRequestDto participationRequestDto = participationService.cancelRequest(userId, requestId);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

}
