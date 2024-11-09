package com.example.dualingo.Converter;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class ListStringConverter {
    @TypeConverter
    public static String fromList(List<String> wordList) {
        return wordList != null ? String.join(",", wordList) : null;
    }

    @TypeConverter
    public static List<String> toList(String data) {
        return data != null ? Arrays.asList(data.split(",")) : null;
    }
}
