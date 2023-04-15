package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest, List<Event> events, Long compId) {
        return Compilation.builder()
                .id(compId)
                .title(updateCompilationRequest.getTitle())
                .pinned(updateCompilationRequest.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents() == null ? null :
                        compilation.getEvents().stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList()))
                .build();
    }

}
