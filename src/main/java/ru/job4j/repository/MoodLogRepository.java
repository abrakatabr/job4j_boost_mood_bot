package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.model.MoodLog;
import ru.job4j.model.User;

import java.util.List;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findByUserId(Long userId);

    @Query("SELECT u FROM User u WHERE NOT EXISTS ("
            + "SELECT 1 FROM MoodLog ml WHERE ml.user = u AND ml.createdAt BETWEEN :startOfDay AND :endOfDay)")
    List<User> findUsersWhoDidNotVoteToday(@Param("startOfDay") long startOfDay,
                                           @Param("endOfDay") long endOfDay);
}
