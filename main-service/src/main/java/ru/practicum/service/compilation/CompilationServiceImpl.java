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
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.CompilationsRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CompilationMapper.toCompilation;
import static ru.practicum.mapper.CompilationMapper.toCompilationDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationsRepository compilationsRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        log.info("Save compilation title: {}", newCompilationDto.getTitle());

        List<Event> events = findEvents(newCompilationDto.getEvents());
        Compilation compilation = toCompilation(newCompilationDto, events);
        Compilation saveAndFlush = compilationsRepository.saveAndFlush(compilation);
        return toCompilationDto(saveAndFlush);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Delete compilation id: {}", compId);

        Compilation compilation = findCompilation(compId);
        compilationsRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update compilation id: {}, title: {}", compId, updateCompilationRequest.getTitle());

        List<Event> events = findEvents(updateCompilationRequest.getEvents());
        findCompilation(compId);
        Compilation compilation = toCompilation(updateCompilationRequest, events, compId);
        Compilation saveAndFlush = compilationsRepository.saveAndFlush(compilation);
        return toCompilationDto(saveAndFlush);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Get compilations");
        Pageable pageable = PageRequest.of(from / size, size);

        if (pinned != null) {
            return compilationsRepository.findAllByPinned(pinned, pageable)
                    .stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
        return compilationsRepository.findAll(pageable)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        log.info("Get compilation id: {}", compId);

        Compilation compilation = findCompilation(compId);
        return toCompilationDto(compilation);
    }

    private Compilation findCompilation(Long compId) {
        return compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }

    private List<Event> findEvents(List<Long> eventIds) {
        return (eventIds == null || eventIds.isEmpty()) ? null : eventRepository.findAllById(eventIds);
    }
}
