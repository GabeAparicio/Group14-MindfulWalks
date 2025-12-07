package com.example.mindfulwalks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CheckpointDao {

    @Insert
    void insertCheckpoint(Checkpoint checkpoint);

    @Update
    void updateCheckpoint(Checkpoint checkpoint);

    @Delete
    void deleteCheckpoint(Checkpoint checkpoint);

    @Query("UPDATE checkpoints SET rating = :rating WHERE id = :id")
    void updateRating(int id, float rating);

    @Query("SELECT * FROM checkpoints ORDER BY id DESC")
    List<Checkpoint> getAllCheckpoints();

    @Query("SELECT * FROM checkpoints WHERE id = :id LIMIT 1")
    Checkpoint getCheckpointById(int id);
}

