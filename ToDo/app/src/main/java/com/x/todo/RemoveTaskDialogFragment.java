package com.x.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
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
