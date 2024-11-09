package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "fill_blank")
public class FillBlank {
    @PrimaryKey
    @NonNull
    private String idFillBlank;

    private String idLecture;
    private String question;
    private String result;
    private List<String> wordList;

    public String getIdFillBlank() {
        return idFillBlank;
    }

    public void setIdFillBlank(String idFillBlank) {
        this.idFillBlank = idFillBlank;
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

    public FillBlank(String idFillBlank, String idLecture, String question, String result, List<String> wordList) {
        this.idFillBlank = idFillBlank;
        this.idLecture = idLecture;
        this.question = question;
        this.result = result;
        this.wordList = wordList;
    }
}
