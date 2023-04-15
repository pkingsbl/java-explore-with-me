package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
