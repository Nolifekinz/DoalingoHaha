package com.example.dualingo.Models;

import java.util.List;

public class Session {
    private String idSession;
    private String sessionTitle;
    private int imgUrl;
    private List<Lecture> lectures;

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

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public Session(String idSession, String sessionTitle, int imgUrl, List<Lecture> lectures) {
        this.idSession = idSession;
        this.sessionTitle = sessionTitle;
        this.imgUrl = imgUrl;
        this.lectures = lectures;
    }
}