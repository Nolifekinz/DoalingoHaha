package com.example.dualingo.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dualingo.Models.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users LIMIT 1")
    User getCurrentUser();

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
