package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import java.util.List;

@Entity(tableName = "arranging")
public class Arranging {
    @PrimaryKey
    @NonNull
    private String idArranging;

    private String idLecture;
    private String question;
    private String result;
    private List<String> wordList;

    public String getIdArranging() {
        return idArranging;
    }

    public void setIdArranging(String idArranging) {
        this.idArranging = idArranging;
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

    public Arranging() {
    }

    public Arranging(String idArranging, String idLecture, String question, String result, List<String> wordList) {
        this.idArranging = idArranging;
        this.idLecture = idLecture;
        this.question = question;
        this.result = result;
        this.wordList = wordList;
    }
}
