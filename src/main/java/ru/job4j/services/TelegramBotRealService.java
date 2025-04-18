package ru.job4j.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.condition.OnRealCondition;
import ru.job4j.model.Content;

@Service
@Conditional(OnRealCondition.class)
public class TelegramBotRealService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotRealService(@Value("${telegram.bot.name}") String botName,
                                  @Value("${telegram.bot.token}") String botToken,
                                  BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        System.out.println("Бин реального телеграм бота создан");
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
        try {
            if (content.getAudio() != null) {
                SendAudio audio = new SendAudio();
                audio.setChatId(content.getChatId());
                audio.setAudio(content.getAudio());
                if (content.getText() != null) {
                    audio.setCaption(content.getText());
                }
                send(audio);
            } else if (content.getText() != null) {
                SendMessage message = new SendMessage();
                message.setChatId(content.getChatId());
                message.setText(content.getText());
                if (content.getMarkup() != null) {
                    message.setReplyMarkup(content.getMarkup());
                }
                send(message);
            } else if (content.getPhoto() != null) {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(content.getChatId());
                photo.setPhoto(content.getPhoto());
                if (content.getText() != null) {
                    photo.setCaption(content.getText());
                }
                send(photo);
            }
        } catch (SentContentException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendAudio audio) {
        try {
            execute(audio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
