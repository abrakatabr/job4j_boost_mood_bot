package ru.job4j.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.event.UserEvent;
import ru.job4j.model.*;
import ru.job4j.repository.AchievementRepository;
import ru.job4j.repository.MoodLogRepository;
import ru.job4j.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final ApplicationEventPublisher publisher;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       ApplicationEventPublisher publisher) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.publisher = publisher;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = new Mood();
        mood.setId(moodId);
        moodLogRepository.save(new MoodLog(user, mood, Instant.now().getEpochSecond()));
        publisher.publishEvent(new UserEvent(this, user));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> searchUser = userRepository.findByClientIdAndChatId(chatId, clientId);
        if (searchUser.isPresent()) {
            List<MoodLog> moodLogs = moodLogRepository.findByUserId(searchUser.get().getId()).stream()
                    .filter(m -> {
                        LocalDateTime createdAt = Instant.ofEpochSecond(m.getCreatedAt()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime startTime = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().minusWeeks(1L);
                        return createdAt.isAfter(startTime);
                    }).collect(Collectors.toList());
                content.setText(formatMoodLogs(moodLogs, "1 week mood log"));
        }
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> searchUser = userRepository.findByClientIdAndChatId(chatId, clientId);
        if (searchUser.isPresent()) {
            List<MoodLog> moodLogs = moodLogRepository.findByUserId(searchUser.get().getId()).stream()
                    .filter(m -> {
                        LocalDateTime createdAt = Instant.ofEpochSecond(m.getCreatedAt()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime startTime = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().minusMonths(1L);
                        return createdAt.isAfter(startTime);
                    }).collect(Collectors.toList());
                content.setText(formatMoodLogs(moodLogs, "1 month mood log"));
        }
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> searchUser = userRepository.findByClientIdAndChatId(chatId, clientId);
        if (searchUser.isPresent()) {
            User user = searchUser.get();
            List<Achievement> userAchievement = achievementRepository.findByUser(user);
            StringBuilder sb = new StringBuilder();
            userAchievement.stream()
                    .forEach(a -> {
                        String formattedDate = formatter.format(Instant.ofEpochSecond(a.getCreateAt()));
                        sb.append(formattedDate).append(": ").append(a.getAward().getTitle()).append(System.lineSeparator());
                    });
            content.setText(sb.toString());
        }
        return Optional.of(content);
    }
}
