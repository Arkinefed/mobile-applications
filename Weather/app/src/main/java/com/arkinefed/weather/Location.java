package com.arkinefed.weather;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Location implements Serializable {
    private String name;
    private double lat;
    private double lon;
    private LocalDateTime lastUpdated;
    private CurrentWeather currentWeather;
    private WeatherForecast weatherForecast;

    public Location() {

    }

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLatUpdatedAsString() {
        if (lastUpdated == null) {
            return "";
        }

        return lastUpdated.getYear() + "-" +
                lastUpdated.getMonth().getValue() + "-" +
                lastUpdated.getDayOfMonth() + " " +
                lastUpdated.getHour() + ":" +
                lastUpdated.getMinute();
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }
}
