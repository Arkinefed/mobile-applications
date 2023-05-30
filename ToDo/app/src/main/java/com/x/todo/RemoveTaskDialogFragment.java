package com.x.todo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class RemoveTaskDialogFragment extends DialogFragment {
    private final List<Task> tasks;
    private final int position;
    private final TasksAdapter tasksAdapter;
    private final TaskListActivity taskListActivity;

    public RemoveTaskDialogFragment(List<Task> tasks, int position, TasksAdapter tasksAdapter, TaskListActivity taskListActivity) {
        this.tasks = tasks;
        this.position = position;
        this.tasksAdapter = tasksAdapter;
        this.taskListActivity = taskListActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.remove_task_message).
                setPositiveButton(R.string.remove, (dialogInterface, i) -> {
                    Task task = tasks.get(position);

                    if (task.isNotification()) {
                        AlarmManager alarmManager = (AlarmManager) taskListActivity.getSystemService(Context.ALARM_SERVICE);

                        Intent alarmIntent = new Intent(taskListActivity, NotificationReceiver.class);
                        alarmIntent.putExtra("id", task.getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(taskListActivity, task.getId(), alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);

                        alarmManager.cancel(pendingIntent);
                    }

                    Thread dbThread = new Thread(() -> {
                        deleteFolder(new File(getContext().getFilesDir() + "/" + tasks.get(position).getFolderName()));

                        TaskDatabase.getInstance(getContext()).taskDao().deleteTaskById(tasks.get(position).getId());
                    });

                    dbThread.start();

                    try {
                        dbThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    taskListActivity.updateTaskList();

                    tasksAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                });

        return builder.create();
    }

    private void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            for (File child : folder.listFiles()) {
                deleteFolder(child);
            }
        }

        folder.delete();
    }
}
