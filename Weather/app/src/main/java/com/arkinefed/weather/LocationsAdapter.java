package com.arkinefed.weather;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder> {
    private ArrayList<Location> locations;
    private FragmentManager fragmentManager;

    public LocationsAdapter(ArrayList<Location> locations, FragmentManager fragmentManager) {
        this.locations = locations;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);

        return new LocationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
        holder.name.setText(locations.get(position).getName());
        holder.lastUpdate.setText(locations.get(position).getLatUpdatedAsString());
        holder.position = position;
        holder.remove.setOnClickListener(view -> {
            RemoveLocationDialogFragment dialogFragment = new RemoveLocationDialogFragment(locations, position, this);
            dialogFragment.show(fragmentManager, "remove_device_dialog");
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView lastUpdate;
        private Button remove;

        private int position;

        public LocationsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), WeatherActivity.class);
                intent.putExtra("position", position);
                view.getContext().startActivity(intent);
            });

            name = itemView.findViewById(R.id.location_name);
            lastUpdate = itemView.findViewById(R.id.location_last_update);
            remove = itemView.findViewById(R.id.location_remove);
        }
    }
}
