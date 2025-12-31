package com.example.timelinelogging.ui.timeline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timelinelogging.R;
import com.example.timelinelogging.adapter.TimelineAdapter;
import com.example.timelinelogging.data.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TimelineAdapter adapter;
    List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerTimeline);

        postList = new ArrayList<>();
        postList.add(new Post("09:00 AM", "Woke up"));
        postList.add(new Post("11:30 AM", "Attended class"));
        postList.add(new Post("02:00 PM", "Worked on Android project"));

        adapter = new TimelineAdapter(postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
