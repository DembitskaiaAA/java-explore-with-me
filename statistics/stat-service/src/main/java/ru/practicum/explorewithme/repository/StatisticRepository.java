package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT new ru.practicum.explorewithme.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT (e.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEnd(Timestamp start, Timestamp end);

    @Query(value = "SELECT new ru.practicum.explorewithme.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT (e.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndWithUnique(Timestamp start, Timestamp end);

    @Query(value = "SELECT new ru.practicum.explorewithme.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT (e.ip) DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndByUris(Timestamp start, Timestamp end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.explorewithme.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT (e.ip)  DESC")
    List<ViewStats> findAllAfterStartAndBeforeEndByUrisWithUnique(Timestamp start, Timestamp end, List<String> uris);
}
