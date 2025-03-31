package com.tm471a.intelligenthealthylifestyle.utils;

import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

public abstract class Typewriter {
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 2000;
    private TextView textView;

    public Typewriter(TextView textView) {
        this.textView = textView;
    }

    public Handler mHandler = new Handler();
    public Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            textView.setText(mText.subSequence(0, mIndex++));

            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }else
                animationEnded();

        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;
        textView.setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public abstract void animationEnded();
}