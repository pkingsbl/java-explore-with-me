package ru.practicum.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hits")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @Column(length = 512, name = "uri")
    private String uri;

    @Column(length = 15, name = "ip", nullable = false)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
