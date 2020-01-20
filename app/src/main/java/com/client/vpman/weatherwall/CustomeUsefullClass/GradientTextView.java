package com.client.vpman.weatherwall.CustomeUsefullClass;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;

import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;

/**
 * Created by rahulchowdhury on 04/08/16.
 */

public class GradientTextView extends MaterialTextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {
            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    ContextCompat.getColor(getContext(), R.color.colorStart),
                    ContextCompat.getColor(getContext(), R.color.colorEnd),
                    Shader.TileMode.CLAMP));
        }
    }
}