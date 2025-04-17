package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Achievement;
import ru.job4j.model.Mood;
import ru.job4j.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AchievementFakeRepository extends CrudRepositoryFake<Achievement, Long>
        implements AchievementRepository {
    public List<Achievement> findByUser(User user) {
        return memory.values().stream()
                .filter(achievement -> achievement.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }
}
