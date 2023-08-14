package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiatorOrderByEventDateDesc(User initiator, Pageable pageable);

    Set<Event> findAllByIdIn(Set<Integer> eventsIds);

    Optional<Event> findByIdAndPublishedOnIsNotNull(Integer id);
}
