package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "listening")
public class Listening {
    @PrimaryKey
    @NonNull
    private String idListening;
    private String idLecture;
    private String question;
    private String result;
    private List<String> wordList;

    public Listening(String idListening, String idLecture, String question, String result, List<String> wordList) {
        this.idListening = idListening;
        this.idLecture = idLecture;
        this.question = question;
        this.result = result;
        this.wordList = wordList;
    }

    public Listening() {
    }

    public String getIdListening() {
        return idListening;
    }

    public void setIdListening(String idListening) {
        this.idListening = idListening;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getWordList() {
        return wordList;
    }

    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }
}
