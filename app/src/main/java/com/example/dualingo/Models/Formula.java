package com.example.dualingo.Models;

public class Formula {
    private String affirmative;
    private String negative;
    private String question;

    public Formula(String affirmative, String negative, String question) {
        this.affirmative = affirmative;
        this.negative = negative;
        this.question = question;
    }

    // Getter và setter
    public String getAffirmative() {
        return affirmative;
    }

    public void setAffirmative(String affirmative) {
        this.affirmative = affirmative;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}