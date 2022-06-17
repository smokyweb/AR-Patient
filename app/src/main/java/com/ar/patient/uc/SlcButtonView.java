package com.ar.patient.uc;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;


public class SlcButtonView extends AppCompatButton {
    public SlcButtonView(Context context) {
        super(context);
        this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
    }

    public SlcButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
    }

    public SlcButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
    }
}