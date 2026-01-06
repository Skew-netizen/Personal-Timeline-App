package com.example.timelinelogging.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.timelinelogging.data.entity.Post;
import com.example.timelinelogging.data.repository.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;
    private LiveData<List<Post>> allPosts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        allPosts = repository.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    public void insert(Post post) {
        repository.insert(post);
    }

    public LiveData<List<Post>> getPostsByTag(String tag) {
        return repository.getPostsByTag(tag);
    }

    public LiveData<Integer> getPostCountForDate(String date) {
        return repository.getPostCountForDate(date);
    }

    public LiveData<Integer> getPostCountByTag(String date, String tag) {
        return repository.getPostCountByTag(date, tag);
    }

    public LiveData<List<Post>> getPostsByDate(String date) {
        return repository.getPostsByDate(date);
    }



}
