package com.example.dualingo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String id = "", username = "", email = "", profilePic = "";
    private List<String> followerList = new ArrayList<>();
    private List<String> followingList = new ArrayList<>();
    private List<String> opponentList = new ArrayList<>();
    private Long exp = 0L;
    private Long rank = 0L;

    private int streak = 0; // Chuỗi học liên tiếp
    private Progress progress; // Tiến trình học tập

    // Constructor đầy đủ
    public User(String id, String email, String username, Long exp, Long rank, int streak, Progress progress) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.exp = exp;
        this.rank = rank;
        this.streak = streak;
        this.progress = progress;
    }

    // Constructor mặc định
    public User() {
        this.progress = new Progress(); // Khởi tạo mặc định
    }

    // Getters và Setters
    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
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

    public void setFollowerList(List<String> followerList) {
        this.followerList = followerList;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<String> followingList) {
        this.followingList = followingList;
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

    public List<String> getOpponentList() {
        return opponentList;
    }

    public void setOpponentList(List<String> opponentList) {
        this.opponentList = opponentList;
    }

    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        profilePic = in.readString();
        followerList = in.createStringArrayList();
        followingList = in.createStringArrayList();
        opponentList = in.createStringArrayList();
        if (in.readByte() == 0) {
            exp = null;
        } else {
            exp = in.readLong();
        }
        if (in.readByte() == 0) {
            rank = null;
        } else {
            rank = in.readLong();
        }
        streak = in.readInt();
        progress = in.readParcelable(Progress.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(profilePic);
        dest.writeStringList(followerList);
        dest.writeStringList(followingList);
        dest.writeStringList(opponentList);
        if (exp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(exp);
        }
        if (rank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rank);
        }
        dest.writeInt(streak);
        dest.writeParcelable(progress, flags);
    }

    // Lớp Progress
    public static class Progress implements Parcelable {
        private String sessionId = "";
        private String lectureId = "";

        public Progress() {
        }

        public Progress(String sessionIndex, String lectureIndex) {
            this.sessionId = sessionIndex;
            this.lectureId = lectureIndex;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionIndex) {
            this.sessionId = sessionIndex;
        }

        public String getLectureId() {
            return lectureId;
        }

        public void setLectureId(String lectureIndex) {
            this.lectureId = lectureIndex;
        }

        protected Progress(Parcel in) {
            sessionId = in.readString();
            lectureId = in.readString();
        }

        public static final Creator<Progress> CREATOR = new Creator<Progress>() {
            @Override
            public Progress createFromParcel(Parcel in) {
                return new Progress(in);
            }

            @Override
            public Progress[] newArray(int size) {
                return new Progress[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(sessionId);
            dest.writeString(lectureId);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}
