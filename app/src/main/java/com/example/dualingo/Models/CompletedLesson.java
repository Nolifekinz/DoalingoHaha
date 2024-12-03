package com.example.dualingo.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "completed_lesson")
public class CompletedLesson {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private String lectureId;
    private int arranging;
    private int fillBlank;
    private int listening;
    private int speaking;
    private int vocabularyLesson;

    public CompletedLesson() {
    }

    public CompletedLesson(String userId, String lectureId, int arranging, int fillBlank, int listening, int speaking, int vocabulary) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.arranging = arranging;
        this.fillBlank = fillBlank;
        this.listening = listening;
        this.speaking = speaking;
        this.vocabularyLesson = vocabulary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public int getArranging() {
        return arranging;
    }

    public void setArranging(int arranging) {
        this.arranging = arranging;
    }

    public int getFillBlank() {
        return fillBlank;
    }

    public void setFillBlank(int fillBlank) {
        this.fillBlank = fillBlank;
    }

    public int getListening() {
        return listening;
    }

    public void setListening(int listening) {
        this.listening = listening;
    }

    public int getSpeaking() {
        return speaking;
    }

    public void setSpeaking(int speaking) {
        this.speaking = speaking;
    }

    public int getVocabularyLesson() {
        return vocabularyLesson;
    }

    public void setVocabularyLesson(int vocabulary) {
        this.vocabularyLesson = vocabulary;
    }
}
