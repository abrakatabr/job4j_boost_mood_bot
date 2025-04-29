package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.model.AdviceStatus;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdviceStatusRepository extends CrudRepository<AdviceStatus, Long> {
    Optional<AdviceStatus> findByUser(User user);

    @Query("select user from AdviceStatus where disable is false")
    List<User> findUsersWhereDisableIsFalse();

    @Transactional
    @Modifying
    @Query("update AdviceStatus a set a.disable = true "
            + "where a.user= :user")
    void setDisableByUserIsTrue(@Param("user") User user);

    @Transactional
    @Modifying
    @Query("update AdviceStatus a set disable = false "
            + "where a.user = :user")
    void setDisableByUserIsFalse(@Param("user") User user);
}
