package ru.job4j.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.AdviceLog;

public class AdviceLogFakeRepository extends CrudRepositoryFake<AdviceLog, Long>
        implements AdviceLogRepository {
}
