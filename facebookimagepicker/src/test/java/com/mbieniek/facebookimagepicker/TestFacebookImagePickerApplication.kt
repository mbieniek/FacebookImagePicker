package com.mbieniek.facebookimagepicker

import android.app.Application


class TestFacebookImagePickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_AppCompat)
    }

}