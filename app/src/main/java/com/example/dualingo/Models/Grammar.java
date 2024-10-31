package com.example.dualingo.Models;

import java.util.List;

public class Grammar {
    private String idGrammar;
    private String title;
    private String description;
    private String example;
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

    // Lớp Formula với constructor rỗng và getter/setter
    public static class Formula {
        private String affirmative;
        private String negative;
        private String question;

        // Constructor rỗng cho Firestore
        public Formula() { }

        public Formula(String affirmative, String negative, String question) {
            this.affirmative = affirmative;
            this.negative = negative;
            this.question = question;
        }

        public String getAffirmative() { return affirmative; }
        public void setAffirmative(String affirmative) { this.affirmative = affirmative; }

        public String getNegative() { return negative; }
        public void setNegative(String negative) { this.negative = negative; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
    }
}
