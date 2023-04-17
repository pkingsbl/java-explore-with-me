package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.request.StatusEnum.CONFIRMED;
import static ru.practicum.mapper.CompilationMapper.toCompilation;
import static ru.practicum.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.mapper.EventMapper.toEventShortDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        log.info("Save compilation title: {}", newCompilationDto.getTitle());

        List<Event> events = findEvents(newCompilationDto.getEvents());
        Compilation compilation = toCompilation(newCompilationDto, events);
        Compilation saveAndFlush = compilationRepository.saveAndFlush(compilation);
        return toCompilationDto(saveAndFlush, getEventShortDto(saveAndFlush));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Delete compilation id: {}", compId);

        Compilation compilation = findCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update compilation id: {}, title: {}", compId, updateCompilationRequest.getTitle());

        Compilation compilation = findCompilation(compId);
        compilation.setEvents(updateCompilationRequest.getEvents() == null ? compilation.getEvents() : findEvents(updateCompilationRequest.getEvents()));
        compilation.setPinned(updateCompilationRequest.getPinned() == null ? compilation.getPinned() : updateCompilationRequest.getPinned());
        compilation.setTitle(updateCompilationRequest.getTitle() == null ? compilation.getTitle() : updateCompilationRequest.getTitle());


        compilation = compilationRepository.saveAndFlush(compilation);
        return toCompilationDto(compilation, getEventShortDto(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Get compilations");
        Pageable pageable = PageRequest.of(from / size, size);

        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable).stream()
                    .map(compilation -> toCompilationDto(compilation, getEventShortDto(compilation)))
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAll(pageable).stream()
                .map(compilation -> toCompilationDto(compilation, getEventShortDto(compilation)))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        log.info("Get compilation id: {}", compId);

        Compilation compilation = findCompilation(compId);
        return toCompilationDto(compilation, getEventShortDto(compilation));
    }

    private Compilation findCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }

    private List<Event> findEvents(List<Long> eventIds) {
        return (eventIds == null || eventIds.isEmpty()) ? null : eventRepository.findAllById(eventIds);
    }

    private Long findConfirmedRequests(Event event) {
        return participationRepository.countByEventIdAndStatus(event.getId(), CONFIRMED);
    }

    private List<EventShortDto> getEventShortDto(Compilation compilation) {
        return compilation.getEvents() == null ? List.of() : compilation.getEvents().stream()
                .map(event -> toEventShortDto(event, findConfirmedRequests(event)))
                .collect(Collectors.toList());
    }
}
