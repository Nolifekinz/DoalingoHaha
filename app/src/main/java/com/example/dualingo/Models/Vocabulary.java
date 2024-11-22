package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocabulary")
public class Vocabulary {
    @PrimaryKey
    @NonNull
    private String idVocabulary;
    private String englishWord;
    private String vietnameseMeaning;
    private String wordType;
    private String example;
    private String pronunciation; // Cách phát âm

    private boolean isExpanded; // Để theo dõi xem item có mở rộng không

    public Vocabulary(String idVocabulary, String englishWord, String vietnameseMeaning, String wordType, String example, String pronunciation) {
        this.idVocabulary = idVocabulary;
        this.englishWord = englishWord;
        this.vietnameseMeaning = vietnameseMeaning;
        this.wordType = wordType;
        this.example = example;
        this.pronunciation = pronunciation;
        this.isExpanded = false;
    }

    public Vocabulary() {
    }

    // Getters và Setters
    public String getIdVocabulary() { return idVocabulary; }
    public String getEnglishWord() { return englishWord; }
    public String getVietnameseMeaning() { return vietnameseMeaning; }
    public String getWordType() { return wordType; }
    public String getExample() { return example; }
    public String getPronunciation() { return pronunciation; } // Getter cho pronunciation
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
    public void setIdVocabulary(@NonNull String idVocabulary) {
        this.idVocabulary = idVocabulary;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setVietnameseMeaning(String vietnameseMeaning) {
        this.vietnameseMeaning = vietnameseMeaning;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public void setExample(String example) {
        this.example = example;
    }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; } // Setter cho pronunciation
}
