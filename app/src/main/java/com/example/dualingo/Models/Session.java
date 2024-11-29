package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "session")
public class Session {
    @PrimaryKey
    @NonNull
    private String idSession;
    private String sessionTitle;
    private String imgUrl;
    private List<String> lecturesId;
    private int orderIndex; // Thứ tự của Session

    // Constructor mặc định
    public Session() {
    }

    // Constructor đầy đủ
    public Session(String idSession, String sessionTitle, String imgUrl, List<String> lecturesId, int orderIndex) {
        this.idSession = idSession;
        this.sessionTitle = sessionTitle;
        this.imgUrl = imgUrl;
        this.lecturesId = lecturesId;
        this.orderIndex = orderIndex;
    }

    // Getters và Setters
    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getLecturesId() {
        return lecturesId;
    }

    public void setLecturesId(List<String> lecturesId) {
        this.lecturesId = lecturesId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
