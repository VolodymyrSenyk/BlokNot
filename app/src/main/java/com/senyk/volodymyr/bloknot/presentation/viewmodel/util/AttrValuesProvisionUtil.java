package com.senyk.volodymyr.bloknot.presentation.viewmodel.util;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class AttrValuesProvisionUtil {

    @ColorInt
    public static int getThemeColor(@NonNull final Resources.Theme theme, int colorAttr) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(colorAttr, typedValue, true);
        return typedValue.data;
    }

    public static float getThemeDimenInPixels(@NonNull final Resources.Theme theme, int dimenAttr) {
        final TypedArray styledAttributes = theme.obtainStyledAttributes(new int[]{dimenAttr});
        float dimenInPixels = styledAttributes.getDimensionPixelSize(0, 0);
        styledAttributes.recycle();
        return dimenInPixels;
    }

    @DrawableRes
    public static int getThemeDrawable(@NonNull final Resources.Theme theme, int drawableAttr) {
        final TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(drawableAttr, typedValue, true);
        return typedValue.resourceId;
    }
}
