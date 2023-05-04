package com.arkinefed.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class RemoveLocationDialogFragment extends DialogFragment {
    private final ArrayList<Location> locations;
    private final int position;
    private final LocationsAdapter locationsAdapter;

    public RemoveLocationDialogFragment(ArrayList<Location> locations, int position, LocationsAdapter locationsAdapter) {
        this.locations = locations;
        this.position = position;
        this.locationsAdapter = locationsAdapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.remove_location_message).
                setPositiveButton(R.string.remove, (dialogInterface, i) -> {
                    locations.remove(position);

                    DataProvider.INSTANCE.saveLocationsToMemory(getContext());

                    locationsAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

                });

        return builder.create();
    }
}
