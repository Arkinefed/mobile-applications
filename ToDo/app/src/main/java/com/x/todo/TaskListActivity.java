package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tasks);

        RecyclerView taskList = findViewById(R.id.tasks_list);

        Thread dbThread = new Thread(() -> {
            tasks = TaskDatabase.getInstance(this).taskDao().getAll();
        });

        dbThread.start();

        try {
            dbThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TasksAdapter locationsAdapter = new TasksAdapter(tasks, getSupportFragmentManager(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        taskList.setAdapter(locationsAdapter);
        taskList.setLayoutManager(layoutManager);
        taskList.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton addTask = findViewById(R.id.add_task);

        addTask.setOnClickListener(view -> {
            AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment(tasks, locationsAdapter, this);
            addTaskDialogFragment.show(getSupportFragmentManager(), "add_task_dialog");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}