package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity {
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (savedInstanceState == null) {
            itemPosition = getIntent().getIntExtra("position", 0);
        } else {
            itemPosition = savedInstanceState.getInt("position");
        }

        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView whenCreated = findViewById(R.id.when_created);
        TextView deadline = findViewById(R.id.deadline);
        TextView status = findViewById(R.id.status);
        TextView notification = findViewById(R.id.notification);
        TextView category = findViewById(R.id.category);
        LinearLayout attachments = findViewById(R.id.attachments);

        
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", itemPosition);
    }
}