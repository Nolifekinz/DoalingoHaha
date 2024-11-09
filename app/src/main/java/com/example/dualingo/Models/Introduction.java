package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "introduction")
public class Introduction {
    @PrimaryKey
    @NonNull
    private String idIntroduction;
    private String title;
    private List<String> vocabularyId;
    private List<String> grammarId;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getIdIntroduction() {
        return idIntroduction;
    }

    public void setIdIntroduction(String idIntroduction) {
        this.idIntroduction = idIntroduction;
    }

    public List<String> getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(List<String> vocabularys) {
        this.vocabularyId = vocabularys;
    }

    public List<String> getGrammarId() {
        return grammarId;
    }

    public void setGrammarId(List<String> grammars) {
        this.grammarId = grammars;
    }

    public Introduction(String idIntroduction, String title, List<String> vocabularyId, List<String> grammarId) {
        this.idIntroduction = idIntroduction;
        this.title = title;
        this.vocabularyId = vocabularyId;
        this.grammarId = grammarId;
    }


    public Introduction() { }
}
