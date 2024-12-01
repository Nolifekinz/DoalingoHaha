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

    @Query("SELECT * FROM users WHERE id=:id")
    User getUserById(String id);

    @Query("UPDATE users SET isNewUser = :isNewUser WHERE id = :userId")
    void updateIsNewUser(String userId, int isNewUser);

    @Query("UPDATE users SET exp = :exp WHERE id = :userId")
    void updateExp(String userId, long exp);

    @Query("DELETE FROM users")
    void deleteAllUsers();

}
