package ru.job4j.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.model.AdviceStatus;
import ru.job4j.repository.AdviceStatusRepository;
import ru.job4j.repository.UserRepository;
import ru.job4j.model.Content;
import ru.job4j.model.User;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final AdviceStatusRepository adviceStatusRepository;
    private final MoodService moodService;
    private final AdviceService adviceService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             AdviceStatusRepository adviceStatusRepository,
                             MoodService moodService,
                             AdviceService adviceService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.adviceStatusRepository = adviceStatusRepository;
        this.moodService = moodService;
        this.adviceService = adviceService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        String text = message.getText();
        long chatId = message.getChatId();
        Long clientId = message.getFrom().getId();
        Optional<Content> content = switch (text) {
            case "/start" -> handleStartCommand(chatId, clientId);
            case "/week_mood_log" -> moodService.weekMoodLogCommand(chatId, clientId);
            case "/month_mood_log" -> moodService.monthMoodLogCommand(chatId, clientId);
            case "/award" -> moodService.awards(chatId, clientId);
            case "/daily_advice" -> adviceService.getAdvice(chatId, clientId);
            case "/switch_advice_status" -> adviceService.switchAdviceStatus(chatId, clientId);
            default -> Optional.empty();
        };
        return content;
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        return user.map(value -> moodService.chooseMood(value, moodId));
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        if (userRepository.findByClientIdAndChatId(clientId, chatId).isEmpty()) {
            var user = new User();
            user.setClientId(clientId);
            user.setChatId(chatId);
            userRepository.save(user);
            adviceStatusRepository.save(new AdviceStatus(user, false));
        }
        var content = new Content(chatId);
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
