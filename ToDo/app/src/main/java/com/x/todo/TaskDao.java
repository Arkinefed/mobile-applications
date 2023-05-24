package com.x.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from task")
    List<Task> getAll();

    @Query("select * from task where title like :title")
    List<Task> findByTitle(String title);

    @Query("select * from task where id = :id limit 1")
    Task findTaskById(int id);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("delete from task where id = :id")
    void deleteTaskById(int id);

    @Update
    void update(Task task);
}
