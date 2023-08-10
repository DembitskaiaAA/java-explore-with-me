package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.id IN :ids")
    List<User> findUsersByIds(List<Integer> ids, Pageable pageable);

    User findByName(String name);
}
