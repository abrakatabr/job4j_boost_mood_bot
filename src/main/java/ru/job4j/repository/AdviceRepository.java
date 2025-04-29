package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Advice;
import ru.job4j.model.Mood;

import java.util.List;

@Repository
public interface AdviceRepository extends CrudRepository<Advice, Long> {
    List<Advice> findAll();
}
