package ru.job4j.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.event.UserEvent;
import ru.job4j.model.*;
import ru.job4j.repository.AchievementRepository;
import ru.job4j.repository.AwardRepository;
import ru.job4j.repository.MoodLogRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final SentContent sentContent;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public AchievementService(MoodLogRepository moodLogRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent,
                              AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
        this.achievementRepository = achievementRepository;
    }

    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        List<MoodLog> moodLogs = moodLogRepository.findByUserId(user.getId());
        List<Mood> moods = moodLogs.stream()
                .sorted(Comparator.comparingLong(MoodLog::getCreatedAt))
                .map(ml -> ml.getMood()).collect(Collectors.toList());
        long daysCount = moods.stream().takeWhile(Mood::isGood).count();
        List<Award> userAwards = achievementRepository.findByUser(user).stream()
                .map(Achievement::getAward)
                .collect(Collectors.toList());
        List<Achievement> newAchievements = awardRepository.findAll().stream()
                .filter(a -> a.getDays() <= daysCount)
                .filter(a -> !userAwards.contains(a))
                .map(a -> new Achievement(user,
                a,
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .collect(Collectors.toList());
        saveAll(newAchievements);
        newAchievements.stream()
                .forEach(a -> {
                    Content content = new Content(user.getChatId());
                    String text = "Вы получили награду: " + a.getAward().getTitle()
                            + System.lineSeparator() + a.getAward().getDescription();
                    content.setText(text);
                    sentContent.sent(content);
                });
    }

    @Transactional
    public void saveAll(Iterable<Achievement> achievements) {
        achievementRepository.saveAll(achievements);
    }
}
