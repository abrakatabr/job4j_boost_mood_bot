package ru.job4j.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.AdviceStatus;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdviceStatusFakeRepository extends CrudRepositoryFake<AdviceStatus, Long>
        implements AdviceStatusRepository {
    @Override
    public Optional<AdviceStatus> findByUser(User user) {
        return memory.values().stream().filter(a -> user.equals(a.getUser())).findFirst();
    }

    @Override
    public List<User> findUsersWhereDisableIsFalse() {
        return memory.values().stream()
                .filter(a -> !a.isDisable()).map(a -> a.getUser()).collect(Collectors.toList());
    }

    @Override
    public void setDisableByUserIsTrue(User user) {
        Optional<AdviceStatus> optionalAdviceStatus = memory.values().stream()
                .filter(a -> user.equals(a.getUser())).findFirst();
        if (optionalAdviceStatus.isPresent()) {
            AdviceStatus adviceStatus = optionalAdviceStatus.get();
            adviceStatus.setDisable(true);
            memory.put(adviceStatus.getId(), adviceStatus);
        }
    }

    @Override
    public void setDisableByUserIsFalse(User user) {
        Optional<AdviceStatus> optionalAdviceStatus = memory.values().stream()
                .filter(a -> user.equals(a.getUser())).findFirst();
        if (optionalAdviceStatus.isPresent()) {
            AdviceStatus adviceStatus = optionalAdviceStatus.get();
            adviceStatus.setDisable(false);
            memory.put(adviceStatus.getId(), adviceStatus);
        }
    }
}
