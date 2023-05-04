package com.arkinefed.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class WeatherDataPart3Fragment extends Fragment {
    private Location location;

    public WeatherDataPart3Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_data_part3, container, false);

        Bundle bundle = getArguments();

        this.location = DataProvider.INSTANCE.getLocation(bundle.getInt("position"));

        TextView forecast0 = view.findViewById(R.id.forecast0);
        TextView forecast0desc = view.findViewById(R.id.forecast0desc);
        TextView forecast1 = view.findViewById(R.id.forecast1);
        TextView forecast1desc = view.findViewById(R.id.forecast1desc);
        TextView forecast2 = view.findViewById(R.id.forecast2);
        TextView forecast2desc = view.findViewById(R.id.forecast2desc);
        TextView forecast3 = view.findViewById(R.id.forecast3);
        TextView forecast3desc = view.findViewById(R.id.forecast3desc);
        TextView forecast4 = view.findViewById(R.id.forecast4);
        TextView forecast4desc = view.findViewById(R.id.forecast4desc);

        forecast0.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getWeatherForecast().getList().get(7).getDt()), TimeZone.getDefault().toZoneId()).toString());
        forecast0desc.setText(location.getWeatherForecast().getList().get(7).getWeather().get(0).getDescription());
        forecast1.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getWeatherForecast().getList().get(15).getDt()), TimeZone.getDefault().toZoneId()).toString());
        forecast1desc.setText(location.getWeatherForecast().getList().get(15).getWeather().get(0).getDescription());
        forecast2.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getWeatherForecast().getList().get(23).getDt()), TimeZone.getDefault().toZoneId()).toString());
        forecast2desc.setText(location.getWeatherForecast().getList().get(23).getWeather().get(0).getDescription());
        forecast3.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getWeatherForecast().getList().get(31).getDt()), TimeZone.getDefault().toZoneId()).toString());
        forecast3desc.setText(location.getWeatherForecast().getList().get(31).getWeather().get(0).getDescription());
        forecast4.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(location.getWeatherForecast().getList().get(39).getDt()), TimeZone.getDefault().toZoneId()).toString());
        forecast4desc.setText(location.getWeatherForecast().getList().get(39).getWeather().get(0).getDescription());

        return view;
    }
}