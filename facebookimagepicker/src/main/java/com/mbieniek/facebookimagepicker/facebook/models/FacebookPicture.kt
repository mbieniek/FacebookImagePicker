package com.mbieniek.facebookimagepicker.facebook.models

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by michaelbieniek on 3/18/18.
 */
data class FacebookPicture(val previewUrl: String, val sourceUrl: String, val id: Long) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(previewUrl)
        writeString(sourceUrl)
        writeLong(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FacebookPicture> = object : Parcelable.Creator<FacebookPicture> {
            override fun createFromParcel(source: Parcel): FacebookPicture = FacebookPicture(source)
            override fun newArray(size: Int): Array<FacebookPicture?> = arrayOfNulls(size)
        }
    }
}