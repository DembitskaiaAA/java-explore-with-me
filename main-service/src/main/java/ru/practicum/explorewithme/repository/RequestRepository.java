package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.common.enums.RequestStatus;
import ru.practicum.explorewithme.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findFirstByEvent_IdAndRequester_Id(Integer eventId, Integer requesterId);

    List<Request> findAllByEvent_IdAndStatusIs(Integer eventId, RequestStatus status);

    List<Request> findAllByRequester_Id(Integer eventId);

    List<Request> findAllByEvent_Id(Integer eventId);
}
