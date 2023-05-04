package com.arkinefed.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WeatherDataPart2Fragment extends Fragment {
    private Location location;

    public WeatherDataPart2Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_data_part2, container, false);

        Bundle bundle = getArguments();

        this.location = DataProvider.INSTANCE.getLocation(bundle.getInt("position"));

        TextView windStrength = view.findViewById(R.id.wind_strength);
        TextView windDirection = view.findViewById(R.id.wind_direction);

        windStrength.setText(location.getCurrentWeather().getWind().getSpeed() + " km/h");
        windDirection.setText(location.getCurrentWeather().getWind().getDeg() + " stopni");

        return view;
    }
}