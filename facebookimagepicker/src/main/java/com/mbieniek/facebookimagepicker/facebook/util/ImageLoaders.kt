package com.mbieniek.facebookimagepicker.facebook.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings
import com.squareup.picasso.Picasso

fun loadImage(context: Context, url: String, imageView: ImageView, placeholder: Drawable) {
    when {
        FacebookImagePickerSettings.glideAvailable -> {
            val options = RequestOptions().placeholder(placeholder)
            Glide.with(context)
                    .setDefaultRequestOptions(options)
                    .load(url)
                    .into(imageView)
        }
        FacebookImagePickerSettings.picassoAvailable -> Picasso.get()
                .load(url)
                .placeholder(placeholder)
                .into(imageView)
        else -> throw RuntimeException("No Image Loading Library Detected")
    }
}

fun loadImage(context: Context, url: String, imageView: ImageView) {
    when {
        FacebookImagePickerSettings.glideAvailable -> {
            Glide.with(context)
                    .load(url)
                    .into(imageView)
        }
        FacebookImagePickerSettings.picassoAvailable -> Picasso.get()
                .load(url)
                .into(imageView)
        else -> throw RuntimeException("No Image Loading Library Detected")
    }
}

fun loadImage(context: Context, url: String, imageView: ImageView, placeholder: Int) {
    when {
        FacebookImagePickerSettings.glideAvailable -> {
            val options = RequestOptions().placeholder(placeholder)
            Glide.with(context)
                    .setDefaultRequestOptions(options)
                    .load(url)
                    .into(imageView)
        }
        FacebookImagePickerSettings.picassoAvailable -> Picasso.get()
                .load(url)
                .placeholder(placeholder)
                .into(imageView)
        else -> throw RuntimeException("No Image Loading Library Detected")
    }
}