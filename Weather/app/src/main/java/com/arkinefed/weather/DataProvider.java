package com.arkinefed.weather;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public enum DataProvider {
    INSTANCE;

    private static final String FILE_NAME = "locations.json";

    private ArrayList<Location> locations;

    public ArrayList<Location> loadLocationsFromMemory(Context context) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }

                inputStream.close();

                String data = stringBuilder.toString();

                locations = new ArrayList<>(Arrays.asList(gson.fromJson(data, Location[].class)));
            } catch (IOException e) {
                locations = new ArrayList<>();
            }
        } else {
            locations = new ArrayList<>();
        }

        return locations;
    }

    public void saveLocationsToMemory(Context context) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            File file = new File(context.getFilesDir(), FILE_NAME);
            String data = gson.toJson(locations);

            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {

        }
    }

    public Location getLocation(int position) {
        return locations.get(position);
    }

    public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }

        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDateTime.parse(jsonElement.getAsString(), formatter);
        }
    }
}
