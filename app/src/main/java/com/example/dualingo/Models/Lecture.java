package com.example.dualingo.Models;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lecture")
public class Lecture {
    @PrimaryKey
    @NonNull
    private String idLecture;
    private String title;
    private String imageResId;
    private boolean isUnlocked;

    // Constructor đầy đủ
    public Lecture(String idLecture, String title, String imageResId, boolean isUnlocked) {
        this.idLecture = idLecture;
        this.title = title;
        this.imageResId = imageResId;
        this.isUnlocked = isUnlocked;
    }

    // Getters và Setters
    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

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

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public Lecture() {
    }

    public Lecture(String idLecture, String title, String imageResId) {
        this.idLecture = idLecture;
        this.title = title;
        this.imageResId = imageResId;
    }
}