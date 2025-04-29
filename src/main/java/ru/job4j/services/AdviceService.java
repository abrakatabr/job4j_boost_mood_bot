package ru.job4j.services;


import org.springframework.stereotype.Service;
import ru.job4j.model.*;
import ru.job4j.repository.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdviceService {
    private final AdviceStatusRepository adviceStatusRepository;
    private final UserRepository userRepository;
    private final MoodLogRepository moodLogRepository;
    private final AdviceRepository adviceRepository;
    private final AdviceLogRepository adviceLogRepository;
    private static final Random RND = new Random(System.currentTimeMillis());

    public AdviceService(AdviceStatusRepository adviceStatusRepository,
                         UserRepository userRepository,
                         MoodLogRepository moodLogRepository,
                         AdviceRepository adviceRepository,
                         AdviceLogRepository adviceLogRepository) {
        this.adviceStatusRepository = adviceStatusRepository;
        this.userRepository = userRepository;
        this.moodLogRepository = moodLogRepository;
        this.adviceRepository = adviceRepository;
        this.adviceLogRepository = adviceLogRepository;
    }

    public Optional<Content> switchAdviceStatus(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> searchUser = userRepository.findByClientIdAndChatId(chatId, clientId);
        if (searchUser.isPresent()) {
            User user = searchUser.get();
            Optional<AdviceStatus> adviceStatus = adviceStatusRepository.findByUser(user);
            if (adviceStatus.isPresent()) {
                boolean isDisable = adviceStatus.get().isDisable();
                if (!isDisable) {
                    adviceStatusRepository.setDisableByUserIsTrue(user);
                    content.setText("Ежедневные советы отключены");
                } else {
                    adviceStatusRepository.setDisableByUserIsFalse(user);
                    content.setText("Ежедневные советы включены");
                }
            }
        }
        return Optional.of(content);
    }

    public Optional<Content> getAdvice(long chatId, Long clientId) {
        var content = new Content(chatId);
        Optional<User> searchUser = userRepository.findByClientIdAndChatId(chatId, clientId);
        if (searchUser.isPresent()) {
            User user = searchUser.get();
            Optional<MoodLog> lastMoodLog = moodLogRepository.findLastMoodLogByUser(user);
            if (lastMoodLog.isPresent()) {
                boolean isGood = lastMoodLog.get().getMood().isGood();
                Advice advice;
                if (isGood) {
                    List<Advice> goodMoodAdvices = adviceRepository.findAll().stream()
                            .filter(a -> a.isMoodIsGood()).collect(Collectors.toList());
                    advice = goodMoodAdvices.get(RND.nextInt(0, goodMoodAdvices.size()));
                    content.setText(advice.getDescription());
                } else {
                    List<Advice> badMoodAdvices = adviceRepository.findAll().stream()
                            .filter(a -> !a.isMoodIsGood()).collect(Collectors.toList());
                    advice = badMoodAdvices.get(RND.nextInt(0, badMoodAdvices.size()));
                    content.setText(advice.getDescription());
                }
                AdviceLog adviceLog = new AdviceLog(user, advice, Instant.now().toEpochMilli());
                adviceLogRepository.save(adviceLog);
            }
        }
        return Optional.of(content);
    }
}
