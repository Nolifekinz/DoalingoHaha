package com.example.dualingo.Models;

public class Lecture {
    private String title;
    private int imageResId; // ID của resource hình ảnh (ví dụ R.drawable.sample_image)

    public Lecture(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    // Getter cho tiêu đề
    public String getTitle() {
        return title;
    }

    // Setter cho tiêu đề
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter cho resource ID của hình ảnh
    public int getImageResId() {
        return imageResId;
    }

    // Setter cho resource ID của hình ảnh
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
