package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Speaking;

import java.util.List;

@Dao
public interface SpeakingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpeaking(Speaking speaking);

    @Query("SELECT * FROM speaking WHERE idSpeaking = :id")
    Speaking getSpeakingById(String id);

    @Query("SELECT * FROM speaking")
    List<Speaking> getAllSpeaks();

    @Query("SELECT * FROM speaking ORDER BY RANDOM() LIMIT 5")
    List<Speaking> getRandomSpeaking();

    @Query("SELECT * FROM speaking WHERE idLecture IN (:lectureIds) ORDER BY RANDOM() LIMIT 3")
    List<Speaking> getSpeakingByLectureIds(List<String> lectureIds);


    @Query("SELECT * FROM speaking WHERE idLecture = :lectureId")
    List<Speaking> getSpeaksByLectureId(String lectureId);

    @Delete
    void deleteSpeaking(Speaking speaking);
}
