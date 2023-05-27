package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private final List<Task> tasks = new ArrayList<>();

    private TasksAdapter tasksAdapter;

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tasks);

        RecyclerView taskList = findViewById(R.id.tasks_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        tasksAdapter = new TasksAdapter(tasks, getSupportFragmentManager(), this);

        taskList.setAdapter(tasksAdapter);
        taskList.setLayoutManager(layoutManager);
        taskList.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton addTask = findViewById(R.id.add_task);

        addTask.setOnClickListener(view -> {
            AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment(tasks, tasksAdapter, this);
            addTaskDialogFragment.show(getSupportFragmentManager(), "add_task_dialog");
        });

        search = findViewById(R.id.search);
        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                updateTaskList();

                return true;
            }
            return false;
        });

        updateTaskList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        boolean hideFinished = sharedPref.getBoolean("hideFinished", false);
        boolean sortMostUrgent = sharedPref.getBoolean("sortMostUrgent", false);

        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.hide_finished).setChecked(hideFinished);
        menu.findItem(R.id.sort_most_urgent).setChecked(sortMostUrgent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (id == R.id.search) {

        } else if (id == R.id.more) {

        } else if (id == R.id.hide_finished) {
            boolean hideFinished = !sharedPref.getBoolean("hideFinished", false);

            editor.putBoolean("hideFinished", hideFinished);
            item.setChecked(hideFinished);
        } else if (id == R.id.sort_most_urgent) {
            boolean sortMostUrgent = !sharedPref.getBoolean("sortMostUrgent", false);

            editor.putBoolean("sortMostUrgent", sortMostUrgent);
            item.setChecked(sortMostUrgent);
        } else {
            return super.onOptionsItemSelected(item);
        }

        editor.apply();
        updateTaskList();

        return true;
    }

    public void updateTaskList() {
        String query = "%" + search.getText().toString() + "%";

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean hideFinished = sharedPref.getBoolean("hideFinished", false);
        boolean sortMostUrgent = sharedPref.getBoolean("sortMostUrgent", false);

        Thread dbThread2 = new Thread(() -> {
            tasks.clear();

            if (sortMostUrgent) {
                if (hideFinished) {
                    tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitleUnfinishedSortMostUrgent(query));
                } else {
                    tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitleSortMostUrgent(query));
                }
            } else {
                if (hideFinished) {
                    tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitleUnfinished(query));
                } else {
                    tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitle(query));
                }
            }
        });

        dbThread2.start();

        try {
            dbThread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        tasksAdapter.notifyDataSetChanged();
    }
}