package com.example.dualingo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users")
public class User implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id = "";
    private String username = "", email = "", profilePic = "";
    private List<String> followerList = new ArrayList<>();
    private List<String> followingList = new ArrayList<>();
    private Long exp = 0L;
    private Long rank = 0L;
    private int streak = 0;
    @Embedded
    private Progress progress;
    private String idBattle;
    private String idWrongQuestion;
    private Long lastStudyDate = 0L;

    public User(String id, String email, String username, Long exp, Long rank, int streak, Progress progress, String idBattle, String idWrongQuestion, Long lastStudyDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.exp = exp;
        this.rank = rank;
        this.streak = streak;
        this.progress = progress;
        this.idBattle = idBattle;
        this.idWrongQuestion = idWrongQuestion;
        this.lastStudyDate = lastStudyDate;
    }

    public User() {
    }

    public String getIdBattle() {
        return idBattle;
    }

    public void setIdBattle(String idBattle) {
        this.idBattle = idBattle;
    }

    public String getIdWrongQuestion() {
        return idWrongQuestion;
    }

    public void setIdWrongQuestion(String idWrongQuestion) {
        this.idWrongQuestion = idWrongQuestion;
    }

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

    public Long getLastStudyDate() {
        return lastStudyDate;
    }

    public void setLastStudyDate(Long lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }

    public void updateStreak() {
        long currentTime = System.currentTimeMillis();
        long oneDayInMillis = 86400000;
        long currentDay = currentTime / oneDayInMillis;
        long lastStudyDay = lastStudyDate / oneDayInMillis;

        if (currentDay == lastStudyDay) {
            return;
        }

        long daysDifference = currentDay - lastStudyDay;
        if (daysDifference == 1) {
            streak++;
        } else {
            streak = 1;
        }

        lastStudyDate = currentTime;
    }



    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        profilePic = in.readString();
        followerList = in.createStringArrayList();
        followingList = in.createStringArrayList();
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
        idBattle = in.readString();
        idWrongQuestion = in.readString();
        lastStudyDate = in.readLong();
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
        dest.writeString(idBattle);
        dest.writeString(idWrongQuestion);
        dest.writeLong(lastStudyDate);
    }

    public static class Progress implements Parcelable {
        private int sessionIndex = 1;
        private int lectureIndex = 1;

        public Progress() {
        }

        public Progress(int sessionIndex, int lectureIndex) {
            this.sessionIndex = sessionIndex;
            this.lectureIndex = lectureIndex;
        }

        public int getSessionIndex() {
            return sessionIndex;
        }

        public void setSessionIndex(int sessionIndex) {
            this.sessionIndex = sessionIndex;
        }

        public int getLectureIndex() {
            return lectureIndex;
        }

        public void setLectureIndex(int lectureIndex) {
            this.lectureIndex = lectureIndex;
        }

        protected Progress(Parcel in) {
            sessionIndex = in.readInt();
            lectureIndex = in.readInt();
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
            dest.writeInt(sessionIndex);
            dest.writeInt(lectureIndex);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

}
