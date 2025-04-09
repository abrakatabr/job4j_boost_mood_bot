package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Mood;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodRepository extends CrudRepository<Mood, Long> {
    List<Mood> findAll();

    Optional<Mood> findById(Long id);
}
