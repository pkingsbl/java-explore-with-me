package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.StatsClientServiceImpl;
import ru.practicum.service.compilation.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationsPublicController {

    private final CompilationService compilationService;
    private final StatsClientServiceImpl client;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size,
            HttpServletRequest request
    ) {
        client.post(request);
        List<CompilationDto> compilationDtos = compilationService.getCompilations(pinned, from, size);
        return new ResponseEntity<>(compilationDtos, HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId, HttpServletRequest request) {
        client.post(request);
        CompilationDto compilationDto = compilationService.getCompilation(compId);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

}
