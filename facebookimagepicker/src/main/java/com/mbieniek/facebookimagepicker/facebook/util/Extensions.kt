package com.mbieniek.facebookimagepicker.facebook.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by michaelbieniek on 3/17/18.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}