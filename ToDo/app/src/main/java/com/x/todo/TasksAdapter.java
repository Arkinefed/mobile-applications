package com.x.todo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private final List<Task> tasks;
    private final FragmentManager fragmentManager;
    private final Context context;

    public TasksAdapter(List<Task> tasks, FragmentManager fragmentManager, Context context) {
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
        } else {
            holder.attachment.setImageDrawable(null);
        }

        if (tasks.get(position).isFinished()) {
            holder.listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_background_completed));
        } else {
            holder.listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_background_todo));
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
        private final ConstraintLayout listItem;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), TaskActivity.class);
                intent.putExtra("id", tasks.get(position).getId());
                view.getContext().startActivity(intent);
            });

            title = itemView.findViewById(R.id.task_title);
            remove = itemView.findViewById(R.id.task_remove);
            attachment = itemView.findViewById(R.id.attachment_image);
            deadline = itemView.findViewById(R.id.deadline);

            listItem = itemView.findViewById(R.id.task_list_item);
        }
    }
}
