package ru.job4j.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Advice;

import java.util.List;

public class AdviceFakeRepository extends CrudRepositoryFake<Advice, Long> implements AdviceRepository {
    @Override
    public List<Advice> findAll() {
        return List.copyOf(memory.values());
    }
}
