package ru.job4j.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.model.Content;
import ru.job4j.model.User;
import ru.job4j.model.Advice;
import ru.job4j.repository.AdviceStatusRepository;
import ru.job4j.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final AdviceService adviceService;
    private final AdviceStatusRepository adviceStatusRepository;
    private final TgUI tgUI;

    public ReminderService(SentContent sentContent,
                           MoodLogRepository moodLogRepository,
                           AdviceService adviceService,
                           AdviceStatusRepository adviceStatusRepository,
                           TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.adviceService = adviceService;
        this.adviceStatusRepository = adviceStatusRepository;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUsers() {
        var startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        var endOfDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        for (var user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay)) {
            var content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }

    @Scheduled(cron = "${advice.alert.period}")
    public void remindAdvice() {
        List<User> users = adviceStatusRepository.findUsersWhereDisableIsFalse();
        users.stream()
                .map(u -> adviceService.getAdvice(u.getChatId(), u.getClientId())).forEach(c -> sentContent.sent(c.get()));
    }
}
