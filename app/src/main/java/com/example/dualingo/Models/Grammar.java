package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "grammar")
public class Grammar {
    @PrimaryKey
    @NonNull
    private String idGrammar;
    private String title;
    private String description;
    private String example;
    @Embedded
    private Formula formula;
    private List<String> keyPoints;
    private List<String> detailedExamples;
    private boolean isExpanded; // Trạng thái mở rộng

    // Constructor rỗng cho Firestore
    public Grammar() { }

    public Grammar(String idGrammar, String title, String description, String example,
                   Formula formula, List<String> keyPoints, List<String> detailedExamples) {
        this.idGrammar = idGrammar;
        this.title = title;
        this.description = description;
        this.example = example;
        this.formula = formula;
        this.keyPoints = keyPoints;
        this.detailedExamples = detailedExamples;
        this.isExpanded = false;
    }

    // Getters và Setters
    public String getIdGrammar() { return idGrammar; }
    public void setIdGrammar(String idGrammar) { this.idGrammar = idGrammar; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }


    public Formula getFormula() { return formula; }
    public void setFormula(Formula formula) { this.formula = formula; }

    public List<String> getKeyPoints() { return keyPoints; }
    public void setKeyPoints(List<String> keyPoints) { this.keyPoints = keyPoints; }

    public List<String> getDetailedExamples() { return detailedExamples; }
    public void setDetailedExamples(List<String> detailedExamples) { this.detailedExamples = detailedExamples; }

    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}