package ru.job4j.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.model.Mood;
import ru.job4j.repository.*;
import ru.job4j.model.User;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = {MoodLogFakeRepository.class, UserFakeRepository.class,
        AchievementFakeRepository.class, MoodService.class})
class MoodServiceTest {
    @Autowired
    @Qualifier("moodLogFakeRepository")
    private MoodLogRepository moodLogRepository;

    @Autowired
    @Qualifier("userFakeRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("achievementFakeRepository")
    private AchievementRepository achievementRepository;

    @MockBean
    RecommendationEngine recommendationEngine;

    @MockBean
    ApplicationEventPublisher publisher;

    @Autowired
    MoodService moodService;

    @Test
    public void whenChooseMoodIsGood() {
        User user = new User(1L);
        Mood mood = new Mood("Хорошее настроение", true);
        moodService.chooseMood(user, mood.getId());
        assertThat(moodLogRepository.findByUserId(user.getId())).isNotEmpty();
        assertThat(moodLogRepository.findByUserId(user.getId()).get(0).getMood()).isEqualTo(mood);
        verify(recommendationEngine).recommendFor(user.getChatId(), mood.getId());
    }
}