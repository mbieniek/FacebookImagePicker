package com.mbieniek.facebookimagepicker.facebook.util

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings

fun getImageViewPlaceholder(context: Context) : Drawable? {
    if (FacebookImagePickerSettings.placeholderDrawableResourceId == 0) {
        return null
    }

    val placeholderDrawable = ContextCompat.getDrawable(context, FacebookImagePickerSettings.placeholderDrawableResourceId)
    val color = Color.parseColor(FacebookImagePickerSettings.placeholderDrawableColor)

    if (placeholderDrawable != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(placeholderDrawable, color)
        } else {
            placeholderDrawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    return placeholderDrawable
}