package com.arkinefed.weather;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class WeatherApiCaller {
    private final static String API_KEY = "7d2c82888cf265792cee9c371ad5345a";

    public static Location getLocationData(String city) throws IOException, CityNotFoundException {
        String urlTemplate = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s";
        String url = String.format(urlTemplate, city, API_KEY);

        String data = getDataFromApi(url);

        Gson gson = new Gson();
        Location[] locations = gson.fromJson(data, Location[].class);

        if (locations != null && locations.length > 0) {
            return locations[0];
        }

        throw new CityNotFoundException();
    }

    public static CurrentWeather getCurrentWeather(Location location) throws IOException {
        String urlTemplate = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric";
        String url = String.format(Locale.US, urlTemplate, location.getLat(), location.getLon(), API_KEY);

        String data = getDataFromApi(url);

        Gson gson = new Gson();

        return gson.fromJson(data, CurrentWeather.class);
    }

    public static WeatherForecast getWeatherForecast(Location location) throws IOException {
        String urlTemplate = "https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=metric";
        String url = String.format(Locale.US, urlTemplate, location.getLat(), location.getLon(), API_KEY);

        String data = getDataFromApi(url);

        Gson gson = new Gson();

        return gson.fromJson(data, WeatherForecast.class);
    }

    private static String getDataFromApi(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        int res = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    public static class CityNotFoundException extends Exception {

    }
}
