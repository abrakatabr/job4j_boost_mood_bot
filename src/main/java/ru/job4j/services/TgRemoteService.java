package ru.job4j.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.model.Mood;
import ru.job4j.repository.MoodRepository;
import ru.job4j.repository.UserRepository;
import ru.job4j.model.User;

import java.util.*;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final TgUI tgUI;
    private final MoodRepository moodRepository;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository,
                           TgUI tgUI,
                           MoodRepository moodRepository) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
        this.moodRepository = moodRepository;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            Optional<Mood> searchMood = moodRepository.findById(Long.parseLong(data));
            if (searchMood.isPresent()) {
                send(new SendMessage(String.valueOf(chatId), searchMood.get().getText()));
            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            long chatId = message.getChatId();
            long clientId = message.getFrom().getId();
            if ("/start".equals(message.getText())) {
                Optional<User> contains = userRepository.findByClientIdAndChatId(clientId, chatId);
                if (contains.isEmpty()) {
                    var user = new User();
                    user.setChatId(chatId);
                    user.setClientId(clientId);
                    userRepository.save(user);
                    System.out.println("User отправлен в БД");
                }
            }
            SendMessage buttonsMessage = new SendMessage();
            buttonsMessage.setChatId(chatId);
            buttonsMessage.setText("Как настроение сегодня?");
            buttonsMessage.setReplyMarkup(tgUI.buildButtons());
            send(buttonsMessage);
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
