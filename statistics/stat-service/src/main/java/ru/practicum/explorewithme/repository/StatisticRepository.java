package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.explorewithme.model.ViewStats(h.app,h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 and ?2 " +
            "AND (h.uri LIKE CONCAT('%', ?3, '%') OR h.uri  IN (?3)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndByUrisWithUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.model.ViewStats(h.app,h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 and ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndWithUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.model.ViewStats(h.app,h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 and ?2 " +
            "AND (h.uri LIKE CONCAT('%', ?3, '%') OR h.uri  IN (?3)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.model.ViewStats(h.app,h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 and ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEnd(LocalDateTime start, LocalDateTime end);
}
