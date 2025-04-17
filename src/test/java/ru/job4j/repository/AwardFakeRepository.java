package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Achievement;
import ru.job4j.model.Award;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AwardFakeRepository extends CrudRepositoryFake<Award, Long>
        implements AwardRepository {
    public List<Award> findAll() {
        return new ArrayList<>(memory.values());
    }
}
