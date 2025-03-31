package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class BotCommandHandler {

    @PostConstruct
    public void init() {
        System.out.println("Bean BotCommandHandler created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean BotCommandHandler will be destroyed");
    }
}
