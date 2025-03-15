package com.tm471a.intelligenthealthylifestyle.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisFormatter extends ValueFormatter {
    private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(new Date((long) value));
    }
}