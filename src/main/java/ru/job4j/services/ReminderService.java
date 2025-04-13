package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.job4j.repository.UserRepository;

@Component
public class ReminderService {
    private final TelegramBotService telegramBotService;
    private final UserRepository userRepository;

    public ReminderService(TelegramBotService telegramBotService, UserRepository userRepository) {
        this.telegramBotService = telegramBotService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean ReminderService created");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean ReminderService will be destroyed");
    }
}
