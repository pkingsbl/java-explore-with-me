package ru.practicum.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name = "apps")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, name = "app")
    private String app;

}
