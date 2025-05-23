package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.model.MoodContent;

@Repository
public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {
}
