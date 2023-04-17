package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.event.StateEventFullEnum;
import ru.practicum.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, Pageable toPageable);

    @Query("SELECT e FROM events AS e " +
            "WHERE (:users IS NULL OR e.initiator.id = :users) " +
            "AND (:states IS NULL OR e.state = :states) " +
            "AND (:categories IS NULL OR e.category.id = :categories) " +
            "AND (:start IS NULL OR e.eventDate  > :start) " +
            "AND (:end IS NULL OR e.eventDate <= :end)"
    )
    List<Event> admSearchEvents(
            @Param("users") List<Long> users,
            @Param("states") List<StateEventFullEnum> states,
            @Param("categories") List<Long> categories,
            @Param("start") LocalDateTime rangeStart,
            @Param("end") LocalDateTime rangeEnd,
            Pageable pageable
    );

    @Query("SELECT e FROM events AS e " +
            "WHERE (:text IS NULL OR lower(e.annotation) like lower(concat('%', :text, '%'))) " +
            "OR (:text IS NULL OR lower(e.description) like lower(concat('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id = :categories) " +
            "AND (:paid IS NULL OR e.paid  = :paid) " +
            "AND (:start IS NULL OR e.eventDate  > :start) " +
            "AND (:end IS NULL OR e.eventDate <= :end)" +
            "AND (:states IS NULL OR e.state = :states)"
    )
    List<Event> pbcSearchEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("states") StateEventFullEnum states,
            Pageable pageable
    );

}
