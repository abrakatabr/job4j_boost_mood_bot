package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class ReminderService {

    @PostConstruct
    public void init() {
        System.out.println("Bean ReminderService created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean ReminderService will be destroyed");
    }
}
