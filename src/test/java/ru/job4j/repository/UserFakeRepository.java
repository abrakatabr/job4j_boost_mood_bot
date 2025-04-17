package ru.job4j.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Award;
import ru.job4j.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserFakeRepository extends CrudRepositoryFake<User, Long>
        implements UserRepository {
    public List<User> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public Optional<User> findByClientIdAndChatId(long clientId, long chatId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByClientId(long clientId) {
        return Optional.empty();
    }
}
