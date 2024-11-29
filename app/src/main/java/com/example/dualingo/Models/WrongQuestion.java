package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "wrong_questions")
public class WrongQuestion {
    @PrimaryKey
    @NonNull
    private String idWrongQuestion;

    private List<String> idWrongArrangingList;
    private List<String> idWrongFillBlankList;
    private List<String> idWrongListeningList;
    private List<String> idWrongSpeakingList;
    private List<String> idWrongVocabularyList;

    public WrongQuestion(@NonNull String idWrongQuestion,
                         List<String> idWrongArrangingList,
                         List<String> idWrongFillBlankList,
                         List<String> idWrongListeningList,
                         List<String> idWrongSpeakingList,
                         List<String> idWrongVocabularyList) {
        this.idWrongQuestion = idWrongQuestion;
        this.idWrongArrangingList = idWrongArrangingList;
        this.idWrongFillBlankList = idWrongFillBlankList;
        this.idWrongListeningList = idWrongListeningList;
        this.idWrongSpeakingList = idWrongSpeakingList;
        this.idWrongVocabularyList = idWrongVocabularyList;
    }

    public WrongQuestion() {
    }

    @NonNull
    public String getIdWrongQuestion() {
        return idWrongQuestion;
    }

    public void setIdWrongQuestion(@NonNull String idWrongQuestion) {
        this.idWrongQuestion = idWrongQuestion;
    }

    public List<String> getIdWrongArrangingList() {
        return idWrongArrangingList;
    }

    public void setIdWrongArrangingList(List<String> idWrongArrangingList) {
        this.idWrongArrangingList = idWrongArrangingList;
    }

    public List<String> getIdWrongFillBlankList() {
        return idWrongFillBlankList;
    }

    public void setIdWrongFillBlankList(List<String> idWrongFillBlankList) {
        this.idWrongFillBlankList = idWrongFillBlankList;
    }

    public List<String> getIdWrongListeningList() {
        return idWrongListeningList;
    }

    public void setIdWrongListeningList(List<String> idWrongListeningList) {
        this.idWrongListeningList = idWrongListeningList;
    }

    public List<String> getIdWrongSpeakingList() {
        return idWrongSpeakingList;
    }

    public void setIdWrongSpeakingList(List<String> idWrongSpeakingList) {
        this.idWrongSpeakingList = idWrongSpeakingList;
    }

    public List<String> getIdWrongVocabularyList() {
        return idWrongVocabularyList;
    }

    public void setIdWrongVocabularyList(List<String> idWrongVocabularyList) {
        this.idWrongVocabularyList = idWrongVocabularyList;
    }
}
