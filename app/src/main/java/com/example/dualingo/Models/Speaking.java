package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "speaking")
public class Speaking {
    @PrimaryKey
    @NonNull
    private String idSpeaking;
    private String idLecture;
    private String question;

    public Speaking(String idSpeaking, String idLecture, String question) {
        this.idSpeaking = idSpeaking;
        this.idLecture = idLecture;
        this.question = question;
    }

    public String getIdSpeaking() {
        return idSpeaking;
    }

    public void setIdSpeaking(String idSpeaking) {
        this.idSpeaking = idSpeaking;
    }

    public String getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(String idLecture) {
        this.idLecture = idLecture;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
