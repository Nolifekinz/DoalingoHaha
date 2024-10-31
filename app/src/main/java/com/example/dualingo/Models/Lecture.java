package com.example.dualingo.Models;

public class Lecture {
    private String idLecture;
    private String title;
    private int imageResId;

    public String getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(String idLecture) {
        this.idLecture = idLecture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public Lecture(String idLecture, String title, int imageResId) {
        this.idLecture = idLecture;
        this.title = title;
        this.imageResId = imageResId;
    }
}