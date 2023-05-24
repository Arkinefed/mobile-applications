package com.x.todo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<String> stringToStringList(String value) {
        if (value == null) {
            return new ArrayList<>();
        }

        return new Gson().fromJson(value, new TypeToken<List<String>>() {
        }.getType());
    }

    @TypeConverter
    public static String stringListToString(List<String> value) {
        if (value == null) {
            return "";
        }

        return new Gson().toJson(value);
    }

    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(String value) {
        return LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String localDateTimeToString(LocalDateTime value) {
        return value.toString();
    }
}
