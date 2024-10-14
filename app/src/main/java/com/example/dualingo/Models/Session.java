package com.example.dualingo.Models;

import java.util.List;

public class Session {
    private String sessionTitle;
    private List<Lecture> lectures;

    public Session(String sessionTitle, List<Lecture> lectures) {
        this.sessionTitle = sessionTitle;
        this.lectures = lectures;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }
}