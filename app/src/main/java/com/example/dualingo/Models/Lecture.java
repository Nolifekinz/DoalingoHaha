package com.example.dualingo.Models;

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
    private int orderIndex; // Thứ tự của Lecture trong danh sách

    // Constructor mặc định
    public Lecture() {
    }

    // Constructor đầy đủ
    public Lecture(String idLecture, String title, String imageResId, int orderIndex) {
        this.idLecture = idLecture;
        this.title = title;
        this.imageResId = imageResId;
        this.orderIndex = orderIndex;
    }

    // Getters và Setters
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

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
