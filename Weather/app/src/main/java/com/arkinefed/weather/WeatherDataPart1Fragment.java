package com.arkinefed.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class WeatherDataPart1Fragment extends Fragment {
    private Location location;

    public WeatherDataPart1Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_data_part1, container, false);

        Bundle bundle = getArguments();

        this.location = DataProvider.INSTANCE.getLocation(bundle.getInt("position"));

        TextView locationName = view.findViewById(R.id.location_name);
        TextView coords = view.findViewById(R.id.coords);
        TextView time = view.findViewById(R.id.time);
        TextView temperature = view.findViewById(R.id.temperature);
        TextView pressure = view.findViewById(R.id.pressure);
        TextView description = view.findViewById(R.id.description);

        ImageView image = view.findViewById(R.id.weather_image);

        locationName.setText(location.getName());
        coords.setText(location.getLon() + " " + location.getLat());
        time.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getCurrentWeather().getDt()), TimeZone.getDefault().toZoneId()).toString());

        SharedPreferences sharedPref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

        boolean celsius = sharedPref.getBoolean("celsius", true);

        if (celsius) {
            temperature.setText(String.valueOf(location.getCurrentWeather().getMain().getTemp()) + " stopni C");
        } else {
            temperature.setText(String.valueOf(Math.round((location.getCurrentWeather().getMain().getTemp() * (9.0 / 5.0) + 32) * 100) / 100.0) + " stopni F");
        }

        pressure.setText(String.valueOf(location.getCurrentWeather().getMain().getPressure()) + " hPa");
        description.setText(location.getCurrentWeather().getWeather().get(0).getDescription());

        String url = "https://openweathermap.org/img/wn/" + location.getCurrentWeather().getWeather().get(0).getIcon() + "@4x.png";

        Glide.with(this)
                .load(url)
                .into(image);

        return view;
    }
}