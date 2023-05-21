package com.x.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        RecyclerView taskList = findViewById(R.id.tasks_list);

        ArrayList<Task> tasks = new ArrayList<>();

        LocalDateTime dateNow = LocalDateTime.now();

        Task task1 = new Task("PAM", "zrobić tę aplikację", dateNow, LocalDateTime.now().plusDays(7), true, "studia", dateNow.toString());
        Task task2 = new Task("Major w CS", "odebrać żetony", dateNow, LocalDateTime.now().plusDays(2), true, "gry", dateNow.toString());

        task2.addAttachment("zdj");
        task2.setFinished(true);

        tasks.add(task1);
        tasks.add(task2);

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
}