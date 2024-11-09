package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Introduction;

import java.util.List;

@Dao
public interface IntroductionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntroduction(Introduction introduction);

    @Query("SELECT * FROM introduction WHERE idIntroduction = :id")
    Introduction getIntroductionById(String id);

    @Query("SELECT * FROM introduction")
    List<Introduction> getAllIntroductions();

    @Delete
    void deleteIntroduction(Introduction introduction);
}

