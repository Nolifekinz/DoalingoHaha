package com.example.dualingo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_exercise_progress")
public class UserExerciseProgress {
    @PrimaryKey
    @NonNull
    private String idProgress;

    private String userId;
    private String idExercise;
    private String exerciseType;
    private boolean isCompleted;
    private String userResult;

    public UserExerciseProgress() {}

    public UserExerciseProgress(String idProgress, String userId, String idExercise, String exerciseType, boolean isCompleted, String userResult) {
        this.idProgress = idProgress;
        this.userId = userId;
        this.idExercise = idExercise;
        this.exerciseType = exerciseType;
        this.isCompleted = isCompleted;
        this.userResult = userResult;
    }

    @NonNull
    public String getIdProgress() {
        return idProgress;
    }

    public void setIdProgress(@NonNull String idProgress) {
        this.idProgress = idProgress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getUserResult() {
        return userResult;
    }

    public void setUserResult(String userResult) {
        this.userResult = userResult;
    }

}
