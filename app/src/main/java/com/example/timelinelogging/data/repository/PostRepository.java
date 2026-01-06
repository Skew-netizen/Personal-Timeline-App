package com.example.timelinelogging.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.timelinelogging.data.dao.PostDao;
import com.example.timelinelogging.data.database.AppDatabase;
import com.example.timelinelogging.data.entity.Post;

import java.util.List;

public class PostRepository {

    private PostDao postDao;
    private LiveData<List<Post>> allPosts;

    public PostRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        postDao = database.postDao();
        allPosts = postDao.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    public void insert(Post post) {
        postDao.insert(post);
    }

    public LiveData<List<Post>> getPostsByTag(String tag) {
        return postDao.getPostsByTag(tag);
    }

    public LiveData<List<Post>> getPostsByDate(String date) {
        return postDao.getPostsByDate(date);
    }


    public LiveData<Integer> getPostCountForDate(String date) {
        return postDao.getPostCountForDate(date);
    }

    public LiveData<Integer> getPostCountByTag(String date, String tag) {
        return postDao.getPostCountByTag(date, tag);
    }
}
