package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.request.StatusEnum;
import ru.practicum.entity.Participation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Long countByEventIdAndStatus(Long id, StatusEnum confirmed);

    List<Participation> findAllByRequesterId(Long userId);

    Participation  findByEventIdAndRequesterId(Long eventId, Long userId);

    Optional<Participation> findByIdAndRequesterId(Long userId, Long requestId);

    Collection<Participation> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);
}
