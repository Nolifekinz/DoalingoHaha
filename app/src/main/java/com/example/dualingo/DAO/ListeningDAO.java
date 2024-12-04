package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Listening;

import java.util.List;

@Dao
public interface ListeningDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListening(Listening listening);

    @Query("SELECT * FROM listening WHERE idListening = :id")
    Listening getListeningById(String id);

    @Query("SELECT * FROM listening WHERE idLecture = :lectureId")
    List<Listening> getListeningsByLectureId(String lectureId);

    @Query("SELECT * FROM listening WHERE idLecture IN (:lectureIds) ORDER BY RANDOM() LIMIT 3")
    List<Listening> getListeningByLectureIds(List<String> lectureIds);

    @Query("SELECT * FROM listening")
    List<Listening> getAllListenings();

    @Query("SELECT * FROM listening ORDER BY RANDOM() LIMIT 5")
    List<Listening> getRandomListening();

    @Delete
    void deleteListening(Listening listening);
}
