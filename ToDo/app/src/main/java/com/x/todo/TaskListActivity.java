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

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private List<Task> tasks;

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

        Thread dbThread = new Thread(() -> {
            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);

            boolean hideFinished = sharedPref.getBoolean("hideFinished", false);

            if (hideFinished) {
                tasks = TaskDatabase.getInstance(this).taskDao().findUnfinished();
            } else {
                tasks = TaskDatabase.getInstance(this).taskDao().getAll();
            }
        });

        dbThread.start();

        try {
            dbThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        tasksAdapter = new TasksAdapter(tasks, getSupportFragmentManager(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

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
                search("%" + textView.getText().toString() + "%");

                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        boolean hideFinished = sharedPref.getBoolean("hideFinished", false);

        menu.findItem(R.id.hide_finished).setChecked(hideFinished);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            search("%" + search.getText().toString() + "%");
        } else if (id == R.id.more) {

        } else if (id == R.id.hide_finished) {
            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            boolean hideFinished = !sharedPref.getBoolean("hideFinished", false);

            item.setChecked(hideFinished);

            editor.putBoolean("hideFinished", hideFinished);
            editor.apply();

            search("%" + search.getText().toString() + "%");
        } else {
            return super.onOptionsItemSelected(item);
        }

        tasksAdapter.notifyDataSetChanged();

        return true;
    }

    private void search(String query) {
        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        boolean hideFinished = sharedPref.getBoolean("hideFinished", false);

        Thread dbThread2 = new Thread(() -> {
            tasks.clear();

            if (hideFinished) {
                tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitleUnfinished(query));
            } else {
                tasks.addAll(TaskDatabase.getInstance(this).taskDao().findByTitle(query));
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