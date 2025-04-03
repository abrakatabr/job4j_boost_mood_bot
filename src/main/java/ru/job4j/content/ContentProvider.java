package ru.job4j.content;

import ru.job4j.model.Content;

public interface ContentProvider {
    Content byMood(Long chatId, Long moodId);
}
