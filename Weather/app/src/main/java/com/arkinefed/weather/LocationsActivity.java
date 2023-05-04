package com.arkinefed.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class LocationsActivity extends AppCompatActivity {
    private final AtomicBoolean appInForeground = new AtomicBoolean(true);
    private final AtomicLong updateFrequency = new AtomicLong();
    private Thread updateThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        boolean celsius = sharedPref.getBoolean("celsius", true);

        FloatingActionButton temperatureUnits = findViewById(R.id.temperature_units);

        if (celsius) {
            temperatureUnits.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.celsius_degrees_foreground));
        } else {
            temperatureUnits.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.fahrenheit_degrees_foreground));
        }

        temperatureUnits.setOnClickListener(view -> {
            boolean c = !sharedPref.getBoolean("celsius", true);

            if (c) {
                temperatureUnits.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.celsius_degrees_foreground));
            } else {
                temperatureUnits.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.fahrenheit_degrees_foreground));
            }

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean("celsius", c);
            editor.apply();
        });

        FloatingActionButton refreshTime = findViewById(R.id.refresh_time);

        int time = sharedPref.getInt("refresh", 0);

        if (time == 0) {
            updateFrequency.set(15);

            refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_15s_foreground));
        } else if (time == 1) {
            updateFrequency.set(10 * 60);

            refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_10m_foreground));
        } else if (time == 2) {
            updateFrequency.set(2 * 60 * 60);

            refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_2h_foreground));
        }

        RecyclerView locationList = findViewById(R.id.locations_list);

        ArrayList<Location> locations = DataProvider.INSTANCE.loadLocationsFromMemory(getApplicationContext());

        LocationsAdapter locationsAdapter = new LocationsAdapter(locations, getSupportFragmentManager());

        if (locations.size() > 0 && locations.get(0).getLastUpdated().plusSeconds(updateFrequency.get()).isBefore(LocalDateTime.now())) {
            updateData(locations, locationsAdapter);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        locationList.setAdapter(locationsAdapter);
        locationList.setLayoutManager(layoutManager);
        locationList.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton addLocation = findViewById(R.id.add_location);

        addLocation.setOnClickListener(view -> {
            AddLocationDialogFragment addDeviceDialogFragment = new AddLocationDialogFragment(locations, locationsAdapter, this);
            addDeviceDialogFragment.show(getSupportFragmentManager(), "add_location_dialog");
        });

        Runnable autoUpdateTask = () -> {
            while (true) {
                try {
                    Thread.sleep(updateFrequency.get() * 1000);

                    if (appInForeground.get()) {
                        updateData(locations, locationsAdapter);
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        };

        refreshTime.setOnClickListener(view -> {
            updateThread.interrupt();

            int t = (sharedPref.getInt("refresh", 0) + 1) % 3;

            if (t == 0) {
                updateFrequency.set(15);

                refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_15s_foreground));
            } else if (t == 1) {
                updateFrequency.set(10 * 60);

                refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_10m_foreground));
            } else if (t == 2) {
                updateFrequency.set(2 * 60 * 60);

                refreshTime.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.refresh_time_2h_foreground));
            }

            updateThread = new Thread(autoUpdateTask);
            updateThread.start();

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("refresh", t);
            editor.apply();
        });

        updateThread = new Thread(autoUpdateTask);
        updateThread.start();

        FloatingActionButton refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(view -> {
            updateThread.interrupt();

            updateData(locations, locationsAdapter);

            updateThread = new Thread(autoUpdateTask);
            updateThread.start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        appInForeground.set(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        appInForeground.set(true);
    }

    private void updateData(ArrayList<Location> locations, LocationsAdapter locationsAdapter) {
        new Thread(() -> {
            try {
                for (int i = 0; i < locations.size(); i++) {
                    Location locationData = WeatherApiCaller.getLocationData(locations.get(i).getName());
                    CurrentWeather currentWeather = WeatherApiCaller.getCurrentWeather(locationData);
                    WeatherForecast weatherForecast = WeatherApiCaller.getWeatherForecast(locationData);

                    locationData.setCurrentWeather(currentWeather);
                    locationData.setWeatherForecast(weatherForecast);
                    locationData.setLastUpdated(LocalDateTime.now());

                    locations.set(i, locationData);
                }

                DataProvider.INSTANCE.saveLocationsToMemory(getApplicationContext());

                runOnUiThread(() -> {
                    locationsAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "data updated", Toast.LENGTH_LONG).show();
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
                });
            } catch (WeatherApiCaller.CityNotFoundException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.city_not_found, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}