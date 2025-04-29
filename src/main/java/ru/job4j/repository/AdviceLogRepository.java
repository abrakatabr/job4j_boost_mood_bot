package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.model.AdviceLog;

@Repository
public interface AdviceLogRepository  extends CrudRepository<AdviceLog, Long> {
}
