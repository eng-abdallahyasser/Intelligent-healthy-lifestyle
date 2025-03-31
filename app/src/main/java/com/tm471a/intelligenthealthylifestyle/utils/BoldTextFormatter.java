package com.tm471a.intelligenthealthylifestyle.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.widget.TextView;

public class BoldTextFormatter {

    public static void formatTextWithBold(TextView textView, String text) {
        if (text == null || text.isEmpty()) {
            textView.setText(""); // Handle empty or null strings
            return;
        }

        SpannableString spannableString = new SpannableString(text);
        int startIndex = -1;
        int endIndex = -1;

        while ((startIndex = text.indexOf("**", endIndex + 1)) != -1) {
            endIndex = text.indexOf("**", startIndex + 2);

            if (endIndex != -1) {
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //Bold the **
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), endIndex, endIndex+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold the closing **
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex + 2, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold the text between the **.

            }else{
                break; // If no closing **, stop.
            }
        }
        textView.setText(spannableString);
    }
}
