package ru.job4j.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.job4j.repository.UserRepository;

@Component
public class ReminderService extends BeanNameAware {
    private final TgRemoteService tgRemoteService;
    private final UserRepository userRepository;

    public ReminderService(TgRemoteService tgRemoteService, UserRepository userRepository) {
        this.tgRemoteService = tgRemoteService;
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

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        for (var user : userRepository.findAll()) {
            var message = new SendMessage();
            message.setChatId(user.getChatId());
            message.setText("Ping");
            tgRemoteService.send(message);
        }
    }
}
