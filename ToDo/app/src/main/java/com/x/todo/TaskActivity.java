package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private int id;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (savedInstanceState == null) {
            id = getIntent().getIntExtra("id", 0);
        } else {
            id = savedInstanceState.getInt("id");
        }

        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView whenCreated = findViewById(R.id.when_created);
        TextView deadline = findViewById(R.id.deadline);
        TextView status = findViewById(R.id.status);
        TextView notification = findViewById(R.id.notification);
        TextView category = findViewById(R.id.category);
        TextView attachmentCount = findViewById(R.id.attachment_count);
        LinearLayout attachments = findViewById(R.id.attachments);

        Thread dbThread = new Thread(() -> {
            task = TaskDatabase.getInstance(this).taskDao().findTaskById(id);
        });

        dbThread.start();

        try {
            dbThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        whenCreated.setText(task.getWhenCreated().toString());
        deadline.setText(task.getDeadline().toString());
        status.setText(task.isFinished() ? R.string.finished : R.string.unfinished);
        notification.setText(task.isNotification() ? R.string.on : R.string.off);
        category.setText(task.getCategory());
        attachmentCount.setText(String.valueOf(task.getAttachments().size()));

        for (String attachment : task.getAttachments()) {
            ImageView iv = new ImageView(this);

            iv.setImageURI(Uri.parse(attachment));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int dp = 7;
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

            layoutParams.setMargins(px, px, px, px);

            iv.setLayoutParams(layoutParams);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setAdjustViewBounds(true);

            attachments.addView(iv);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("id", id);
    }
}