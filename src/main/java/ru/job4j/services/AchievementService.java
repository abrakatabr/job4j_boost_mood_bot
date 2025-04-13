package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class AchievementService {

    @PostConstruct
    public void init() {
        System.out.println("Bean AchievementService created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean AchievementService will be destroyed");
    }
}
