package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService extends BeanNameAware {

    @PostConstruct
    public void init() {
        System.out.println("Bean TelegramBotService created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean TelegramBotService will be destroyed");
    }
}
