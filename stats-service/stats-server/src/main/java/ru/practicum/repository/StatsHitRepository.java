package ru.practicum.repository;

import ru.practicum.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsHitRepository  extends JpaRepository<Hit, Long> {

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

}
