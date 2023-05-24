package com.x.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class RemoveTaskDialogFragment extends DialogFragment {
    private final List<Task> tasks;
    private final int position;
    private final TasksAdapter tasksAdapter;

    public RemoveTaskDialogFragment(List<Task> tasks, int position, TasksAdapter tasksAdapter) {
        this.tasks = tasks;
        this.position = position;
        this.tasksAdapter = tasksAdapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.remove_task_message).
                setPositiveButton(R.string.remove, (dialogInterface, i) -> {
                    Thread dbThread = new Thread(() -> {
                        TaskDatabase.getInstance(getContext()).taskDao().delete(tasks.get(position));
                    });

                    dbThread.start();

                    try {
                        dbThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    tasks.remove(position);

                    tasksAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                });

        return builder.create();
    }
}
