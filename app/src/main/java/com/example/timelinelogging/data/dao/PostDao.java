package com.example.timelinelogging.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.timelinelogging.data.entity.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);

    @Query("SELECT * FROM posts ORDER BY postId DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM posts WHERE tag = :tag ORDER BY postId DESC")
    LiveData<List<Post>> getPostsByTag(String tag);

}
