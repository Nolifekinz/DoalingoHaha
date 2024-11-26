package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dualingo.Converter.ListStringConverter;

import java.util.List;

@Entity(tableName = "session")
public class Session {
    @PrimaryKey
    @NonNull
    private String idSession;
    private String sessionTitle;
    private String imgUrl;

    private List<String> lecturesId;
    private int isUnlocked; // Trạng thái mở khóa Session, sử dụng 0 và 1

    // Constructor mặc định
    public Session() {
    }

    // Constructor đầy đủ
    public Session(String idSession, String sessionTitle, String imgUrl, List<String> lecturesId, int isUnlocked) {
        this.idSession = idSession;
        this.sessionTitle = sessionTitle;
        this.imgUrl = imgUrl;
        this.lecturesId = lecturesId;
        this.isUnlocked = isUnlocked;
    }

    // Getters và Setters
    public int getIsUnlocked() {
        return isUnlocked;
    }

    public void setIsUnlocked(int isUnlocked) {
        this.isUnlocked = isUnlocked;
    }

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
}
