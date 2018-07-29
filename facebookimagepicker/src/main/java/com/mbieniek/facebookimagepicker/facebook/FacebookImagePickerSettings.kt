package com.mbieniek.facebookimagepicker.facebook

/**
 * Created by michaelbieniek on 3/30/18.
 */
object FacebookImagePickerSettings {
    var maximumSelectableImages :Int = 6
    var imageGridSpanCount :Int = 3
    var albumActivityTitle : String = "Album(s)"
    var imagesSelectedText : String = "Select (%d)"
    var maximumImagesSelectedText : String = "You\'ve reached the maximum limit of photos that can be selected!"
    var placeholderDrawableColor : String = "#000000"

    val picassoAvailable = picassoCheck()
    val glideAvailable = glideCheck()

    private fun picassoCheck(): Boolean {
        try {
            Class.forName("com.squareup.picasso.Picasso")
            return true
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
        return false
    }

    private fun glideCheck(): Boolean {
        try {
            Class.forName("com.bumptech.glide.Glide")
            return true
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
        return false
    }
}