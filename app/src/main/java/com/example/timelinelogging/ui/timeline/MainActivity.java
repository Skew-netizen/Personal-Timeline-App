package com.example.timelinelogging.ui.timeline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timelinelogging.R;
import com.example.timelinelogging.adapter.TimelineAdapter;
import com.example.timelinelogging.data.entity.Post;
import com.example.timelinelogging.viewmodel.PostViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PostViewModel postViewModel;
    private TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerTimeline);
        adapter = new TimelineAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        postViewModel.getAllPosts().observe(this, posts -> {
            adapter.setPostList(posts);
        });

        // Temporary test insert
        postViewModel.insert(new Post("Started Room Database", "10:00 AM"));
    }
}
