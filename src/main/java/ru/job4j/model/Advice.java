package ru.job4j.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_advice")
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private boolean moodIsGood;

    public Advice() { }

    public Advice(String description, boolean moodIsGood) {
        this.description = description;
        this.moodIsGood = moodIsGood;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMoodIsGood() {
        return moodIsGood;
    }

    public void setMoodIsGood(boolean moodIsGood) {
        this.moodIsGood = moodIsGood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Advice advice = (Advice) o;
        return Objects.equals(id, advice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
