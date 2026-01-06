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

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.timelinelogging.utils.TimeUtils;


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

        findViewById(R.id.fabAddPost).setOnClickListener(v -> showAddPostDialog());
    }

    private void showAddPostDialog() {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_post, null);

        EditText etPost = view.findViewById(R.id.etPostContent);

        new AlertDialog.Builder(this)
                .setTitle("New Timeline Entry")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {

                    String content = etPost.getText().toString().trim();

                    if (!content.isEmpty()) {
                        String time = TimeUtils.getCurrentTime();
                        Post post = new Post(content, time);
                        postViewModel.insert(post);
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
