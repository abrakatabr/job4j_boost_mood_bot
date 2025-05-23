package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Mood;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MoodFakeRepository
        extends CrudRepositoryFake<Mood, Long>
        implements MoodRepository {

    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }
}
