package com.example.timelinelogging.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.timelinelogging.data.entity.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM posts ORDER BY postId DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM posts WHERE tag = :tag ORDER BY postId DESC")
    LiveData<List<Post>> getPostsByTag(String tag);

    @Query("SELECT COUNT(*) FROM posts WHERE date = :date")
    LiveData<Integer> getPostCountForDate(String date);

    @Query("SELECT COUNT(*) FROM posts WHERE date = :date AND tag = :tag")
    LiveData<Integer> getPostCountByTag(String date, String tag);

    @Query("SELECT * FROM posts WHERE date = :date ORDER BY time DESC")
    LiveData<List<Post>> getPostsByDate(String date);

}
