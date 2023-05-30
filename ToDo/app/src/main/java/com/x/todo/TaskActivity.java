package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            id = getIntent().getIntExtra("id", 0);
        } else {
            id = savedInstanceState.getInt("id");
        }

        EditText title = findViewById(R.id.title);
        EditText description = findViewById(R.id.description);
        TextView whenCreated = findViewById(R.id.when_created);
        TextView deadline = findViewById(R.id.deadline);
        CheckBox status = findViewById(R.id.status);
        CheckBox notification = findViewById(R.id.notification);
        Spinner category = findViewById(R.id.category);
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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(task.getTitle());

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        whenCreated.setText(task.getWhenCreated().toString());
        deadline.setText(task.getDeadline().toString());
        status.setChecked(task.isFinished());
        notification.setChecked(task.isNotification());

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(spinnerAdapter);

        category.setSelection(spinnerAdapter.getPosition(task.getCategory()));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {

        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}