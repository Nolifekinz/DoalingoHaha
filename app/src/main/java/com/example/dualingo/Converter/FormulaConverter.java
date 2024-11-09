package com.example.dualingo.Converter;

import androidx.room.TypeConverter;

import com.example.dualingo.Models.Formula;
import com.google.gson.Gson;

public class FormulaConverter {
    @TypeConverter
    public static String fromFormula(Formula formula) {
        return new Gson().toJson(formula);
    }

    @TypeConverter
    public static Formula toFormula(String formulaString) {
        return new Gson().fromJson(formulaString, Formula.class);
    }
}

