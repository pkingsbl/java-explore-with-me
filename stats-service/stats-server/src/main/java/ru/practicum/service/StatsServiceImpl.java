package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.GetStatsRequest;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.entity.App;
import ru.practicum.entity.Hit;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsAppRepository;
import ru.practicum.repository.StatsHitRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static ru.practicum.mapper.HitMapper.mapToHit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsHitRepository statsHitRepository;
    private final StatsAppRepository statsAppRepository;

    @Override
    @Transactional
    public void postHit(HitDto hitDto) {
        log.info("Post hit for app: {}", hitDto.getApp());

        App app = statsAppRepository.findByApp(hitDto.getApp());
        if (app == null) {
            app = statsAppRepository
                    .saveAndFlush(App.builder()
                            .app(hitDto.getApp())
                            .build());
        }
        statsHitRepository.saveAndFlush(mapToHit(hitDto, app));
    }

    @Override
    public List<StatsDto> getStats(GetStatsRequest statsRequest) {
        log.info("Get statistic for from: {} to: {} unique: {} for {}",
                statsRequest.getStart(), statsRequest.getEnd(),
                statsRequest.getUnique(), statsRequest.getUris() != null ? statsRequest.getUris() : "all");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(statsRequest.getStart(), dtf);
        LocalDateTime end = LocalDateTime.parse(statsRequest.getEnd(), dtf);

        List<Hit> hitList = statsHitRepository.findAllByTimestampBetween(start, end);

        if (statsRequest.getUnique()) {
            hitList = getUniqueHits(hitList);
        }
        if (statsRequest.getUris() != null) {
            hitList = getUriHits(statsRequest, hitList);
        }

        return getStatsHits(hitList, hitList);
    }

    private List<StatsDto> getStatsHits(List<Hit> hitList, List<Hit> hits) {
        return hitList.stream()
                .map(HitMapper::toStatsDto)
                .peek(statsDto -> statsDto.setHits(countByUri(hits, statsDto.getUri())))
                .distinct()
                .sorted(Comparator.comparingInt(StatsDto::getHits).reversed())
                .collect(Collectors.toList());
    }

    private static List<Hit> getUriHits(GetStatsRequest statsRequest, List<Hit> hitList) {
        return hitList.stream()
                .filter(hit -> statsRequest.getUris().stream().anyMatch(rq -> rq.equalsIgnoreCase(hit.getUri())))
                .collect(Collectors.toList());
    }

    private static List<Hit> getUniqueHits(List<Hit> hitList) {
        List<Hit> uniqueHits = new ArrayList<>();
        hitList.forEach(hit -> {
            boolean noneMatch = uniqueHits.stream()
                    .noneMatch(uHit -> uHit.getIp().equals(hit.getIp()) && uHit.getUri().equals(hit.getUri()));
            if (noneMatch) {
                uniqueHits.add(hit);
            }
        });
        return uniqueHits;
    }

    private Integer countByUri(List<Hit> hitList, String uri) {
        return (int) hitList.stream().filter(hit -> hit.getUri().equals(uri)).count();
    }
}