package ru.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.GetStatsRequest;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    ResponseEntity<Void> postHit(@Valid @RequestBody HitDto hitDto) {
        statsService.postHit(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    ResponseEntity<List<StatsDto>> getStats(@RequestParam String start, @RequestParam String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {
        List<StatsDto> statsDtos = statsService.getStats(GetStatsRequest.of(start, end, uris, unique));
        return new ResponseEntity<>(statsDtos, HttpStatus.OK);
    }

}
