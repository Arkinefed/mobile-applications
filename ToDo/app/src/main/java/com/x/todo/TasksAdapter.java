package com.x.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private ArrayList<Task> tasks;
    private FragmentManager fragmentManager;
    private Context context;

    public TasksAdapter(ArrayList<Task> tasks, FragmentManager fragmentManager, Context context) {
        this.tasks = tasks;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_item, parent, false);

        return new TasksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        holder.title.setText(tasks.get(position).getTitle());
        holder.deadline.setText(tasks.get(position).getDeadline().toString());

        holder.remove.setOnClickListener(view -> {
            RemoveTaskDialogFragment dialogFragment = new RemoveTaskDialogFragment(tasks, position, this);
            dialogFragment.show(fragmentManager, "remove_device_dialog");
        });

        if (tasks.get(position).getAttachments().size() > 0) {
            holder.attachment.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_attach_file_24));
        }

        holder.position = holder.getAbsoluteAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final Button remove;
        private final ImageView attachment;
        private final TextView deadline;

        private int position;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {

            });

            title = itemView.findViewById(R.id.task_title);
            remove = itemView.findViewById(R.id.task_remove);
            attachment = itemView.findViewById(R.id.attachment_image);
            deadline = itemView.findViewById(R.id.deadline);
        }
    }
}
