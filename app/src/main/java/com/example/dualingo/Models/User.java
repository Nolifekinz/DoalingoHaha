package com.example.dualingo.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    public User(String id, String email, String username, Long exp, Long rank) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.exp = exp;
        this.rank = rank;
    }
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<String> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<String> folowerList) {
        this.followerList = folowerList;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<String> folowingList) {
        this.followingList = folowingList;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    String id="",username="",email="",profilePic="";
    List<String> followerList = new ArrayList<>();
    List<String> followingList = new ArrayList<>();
    Long exp = 0L;
    Long rank = 0L;
}
