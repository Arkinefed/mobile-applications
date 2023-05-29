package com.x.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from task where title like :title")
    List<Task> findByTitle(String title);

    @Query("select * from task where title like :title and category in (:categories)")
    List<Task> findByTitleWithCategories(String title, List<String> categories);

    @Query("select * from task where title like :title order by deadline asc")
    List<Task> findByTitleSortMostUrgent(String title);

    @Query("select * from task where title like :title and category in (:categories) order by deadline asc")
    List<Task> findByTitleSortMostUrgentWithCategories(String title, List<String> categories);

    @Query("select * from task where title like :title and finished = 0")
    List<Task> findByTitleUnfinished(String title);

    @Query("select * from task where title like :title and finished = 0 and category in (:categories)")
    List<Task> findByTitleUnfinishedWithCategories(String title, List<String> categories);

    @Query("select * from task where title like :title and finished = 0 order by deadline asc")
    List<Task> findByTitleUnfinishedSortMostUrgent(String title);

    @Query("select * from task where title like :title and finished = 0 and category in (:categories) order by deadline asc")
    List<Task> findByTitleUnfinishedSortMostUrgentWithCategories(String title, List<String> categories);

    @Query("select * from task where id = :id limit 1")
    Task findTaskById(int id);

    @Insert
    void insert(Task task);

    @Query("delete from task where id = :id")
    void deleteTaskById(int id);

    @Update
    void update(Task task);
}
