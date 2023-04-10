package ru.practicum.repository;

import ru.practicum.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsAppRepository extends JpaRepository<App, Long> {

    App findByApp(String app);

}
