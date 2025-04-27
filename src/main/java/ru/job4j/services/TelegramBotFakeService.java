package ru.job4j.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.condition.OnFakeCondition;
import ru.job4j.model.Content;

@Service
@Conditional(OnFakeCondition.class)
public class TelegramBotFakeService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotFakeService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        System.out.println("Бин фейкового телеграм бота создан");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        if (content.getAudio() != null) {
            StringBuilder audio = new StringBuilder("Отправлено аудиосообщение.").append(System.lineSeparator());
            if (content.getText() != null) {
                audio.append("Текстовое сообщение к аудио: ").append(content.getText()).append(System.lineSeparator());
            }
        } else if (content.getText() != null) {
            StringBuilder text = new StringBuilder("Отправлено текстовое сообщение.").append(System.lineSeparator());
            if (content.getMarkup() != null) {
                text.append("С текстовым сообщением отправлена разметка.").append(System.lineSeparator());
            }
        } else if (content.getPhoto() != null) {
            StringBuilder photo = new StringBuilder("Отправлено фото.").append(System.lineSeparator());
            if (content.getText() != null) {
                photo.append("Текстовое сообщение к фото: ").append(content.getText()).append(System.lineSeparator());
            }
        }
    }
}
