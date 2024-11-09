package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "vocabulary_lesson")
public class VocabularyLesson {
    @PrimaryKey
    @NonNull
    private String idVocabularyLesson;
    private String idLecture;
    private String question;
    private String imgUrl;
    private String result;
    private List<String> wordList;

    public String getIdVocabularyLesson() {
        return idVocabularyLesson;
    }

    public void setIdVocabularyLesson(String idVocabularyLesson) {
        this.idVocabularyLesson = idVocabularyLesson;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public VocabularyLesson(String idVocabularyLesson, String idLecture, String question, String imgUrl, String result, List<String> wordList) {
        this.idVocabularyLesson = idVocabularyLesson;
        this.idLecture = idLecture;
        this.question = question;
        this.imgUrl = imgUrl;
        this.result = result;
        this.wordList = wordList;
    }
}
