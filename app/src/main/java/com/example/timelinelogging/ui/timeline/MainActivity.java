package com.example.timelinelogging.ui.timeline;

import android.Manifest;
import android.app.Notification;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timelinelogging.R;
import com.example.timelinelogging.adapter.TimelineAdapter;
import com.example.timelinelogging.data.entity.Post;
import com.example.timelinelogging.utils.NotificationUtils;
import com.example.timelinelogging.utils.PDFUtils;
import com.example.timelinelogging.viewmodel.PostViewModel;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timelinelogging.utils.TimeUtils;


import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PostViewModel postViewModel;
    private TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerTimeline);
        adapter = new TimelineAdapter(new ArrayList<>(), this::showPostOptionsDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        String todayDate = TimeUtils.getCurrentDate();

        TextView tvSummary = findViewById(R.id.tvDailySummary);

        postViewModel.getPostCountForDate(todayDate)
                .observe(this, count -> {
                    tvSummary.setText("Today's Posts: " + count);
                });

        // Default: show all posts
        postViewModel.getAllPosts().observe(this, posts -> {
            adapter.setPostList(posts);
        });

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // month starts from 0 in CalendarView, so add 1
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            // Observe posts for selected date
            postViewModel.getPostsByDate(selectedDate)
                    .observe(MainActivity.this, posts -> {
                        adapter.setPostList(posts);
                    });

            // Optionally update daily summary
            postViewModel.getPostCountForDate(selectedDate)
                    .observe(MainActivity.this, count -> {
                        tvSummary.setText("Posts on " + selectedDate + ": " + count);
                    });
        });



        // TAG FILTER LOGIC
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);

        ArrayAdapter<CharSequence> filterAdapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.post_tags,
                        android.R.layout.simple_spinner_item
                );

        filterAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerFilter.setAdapter(filterAdapter);

        spinnerFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = parent.getItemAtPosition(position).toString();

                postViewModel.getPostsByTag(selectedTag)
                        .observe(MainActivity.this, posts -> {
                            adapter.setPostList(posts);
                        });
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        findViewById(R.id.fabAddPost).setOnClickListener(v -> showAddPostDialog());
        findViewById(R.id.fabExportPDF).setOnClickListener(v -> {
            postViewModel.getAllPosts()
                    .observe(MainActivity.this, posts -> {
                        if (posts != null && !posts.isEmpty()) {
                            PDFUtils.exportPostsToPDF(MainActivity.this, posts, "Timeline_Export");
                        }
                    });
        });

        NotificationUtils.createChannel(this);
        Notification notification = new NotificationCompat.Builder(this, NotificationUtils.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Daily Journal Reminder")
                .setContentText("Don’t forget to log your day ✍️")
                .setAutoCancel(true)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1, notification);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        prefs.edit().putBoolean("reminder_enabled", true).apply();


    }


    private void showAddPostDialog() {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_post, null);

        EditText etPost = view.findViewById(R.id.etPostContent);
        Spinner spinnerTag = view.findViewById(R.id.spinnerTag);

        ArrayAdapter<CharSequence> adapterSpinner =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.post_tags,
                        android.R.layout.simple_spinner_item
                );

        adapterSpinner.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerTag.setAdapter(adapterSpinner);

        new AlertDialog.Builder(this)
                .setTitle("New Timeline Entry")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {

                    String content = etPost.getText().toString().trim();

                    if (!content.isEmpty()) {
                        String time = TimeUtils.getCurrentTime();
                        String tag = spinnerTag.getSelectedItem().toString();
                        String date = TimeUtils.getCurrentDate();
                        Post post = new Post(content, time, tag, date);
                        postViewModel.insert(post);
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPostOptionsDialog(Post post) {

        String[] options = {"Edit", "Delete"};

        new AlertDialog.Builder(this)
                .setTitle("Post Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditPostDialog(post);
                    } else if (which == 1) {
                        postViewModel.delete(post);
                    }
                })
                .show();
    }
    private void showEditPostDialog(Post post) {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_post, null);

        EditText etPost = view.findViewById(R.id.etPostContent);
        Spinner spinnerTag = view.findViewById(R.id.spinnerTag);

        etPost.setText(post.getContent());

        ArrayAdapter<CharSequence> adapterSpinner =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.post_tags,
                        android.R.layout.simple_spinner_item
                );
        spinnerTag.setAdapter(adapterSpinner);

        if (post.getTag() != null) {
            int spinnerPosition = adapterSpinner.getPosition(post.getTag());
            spinnerTag.setSelection(spinnerPosition);
        }

        new AlertDialog.Builder(this)
                .setTitle("Edit Post")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {

                    post.setContent(etPost.getText().toString());
                    post.setTag(spinnerTag.getSelectedItem().toString());

                    postViewModel.update(post);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}
