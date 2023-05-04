package com.arkinefed.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddLocationDialogFragment extends DialogFragment {
    private ArrayList<Location> locations;
    LocationsAdapter locationsAdapter;
    private LocationsActivity locationsActivity;

    public AddLocationDialogFragment(ArrayList<Location> locations, LocationsAdapter locationsAdapter, LocationsActivity locationsActivity) {
        this.locations = locations;
        this.locationsAdapter = locationsAdapter;
        this.locationsActivity = locationsActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.dialog_add_location, null);

        builder.setView(ll)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    EditText location = ll.findViewById(R.id.location);

                    new Thread(() -> {
                        try {
                            Location locationData = WeatherApiCaller.getLocationData(location.getText().toString());
                            CurrentWeather currentWeather = WeatherApiCaller.getCurrentWeather(locationData);
                            WeatherForecast weatherForecast = WeatherApiCaller.getWeatherForecast(locationData);

                            locationData.setCurrentWeather(currentWeather);
                            locationData.setWeatherForecast(weatherForecast);
                            locationData.setLastUpdated(LocalDateTime.now());

                            locations.add(locationData);
                            DataProvider.INSTANCE.saveLocationsToMemory(locationsActivity.getApplicationContext());

                            locationsActivity.runOnUiThread(() -> {
                                locationsAdapter.notifyDataSetChanged();
                            });
                        } catch (IOException e) {
                            locationsActivity.runOnUiThread(() -> {
                                Toast.makeText(locationsActivity, R.string.connection_error, Toast.LENGTH_LONG).show();
                            });
                        } catch (WeatherApiCaller.CityNotFoundException e) {
                            locationsActivity.runOnUiThread(() -> {
                                Toast.makeText(locationsActivity, R.string.city_not_found, Toast.LENGTH_LONG).show();
                            });
                        }
                    }).start();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> AddLocationDialogFragment.this.getDialog().cancel());
        return builder.create();
    }
}
