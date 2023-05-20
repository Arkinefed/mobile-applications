package com.x.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class RemoveTaskDialogFragment extends DialogFragment {
    private final ArrayList<Task> tasks;
    private final int position;
    private final TasksAdapter tasksAdapter;

    public RemoveTaskDialogFragment(ArrayList<Task> tasks, int position, TasksAdapter tasksAdapter) {
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
                    tasks.remove(position);
                    tasksAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                });

        return builder.create();
    }
}
