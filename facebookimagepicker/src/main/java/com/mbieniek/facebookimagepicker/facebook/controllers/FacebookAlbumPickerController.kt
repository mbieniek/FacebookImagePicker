package com.mbieniek.facebookimagepicker.facebook.controllers

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.mbieniek.android.ui.facebook.adapters.FacebookAlbumAdapter
import com.facebook.AccessToken
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_ALBUM_ID_KEY
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_ALBUM_NAME_KEY
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerActivity
import com.mbieniek.facebookimagepicker.facebook.data.FacebookDataManager
import com.mbieniek.facebookimagepicker.facebook.models.FacebookAlbum
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by michaelbieniek on 3/17/18.
 */

const val PERMISSION_USER_PHOTOS = "user_photos"

class FacebookAlbumPickerController(val activity: Activity) {

    var facebookAlbumAdapter : FacebookAlbumAdapter = FacebookAlbumAdapter(object : FacebookAlbumAdapter.AlbumSelectedListener {
        override fun albumSelected(selectedAlbum: FacebookAlbum) {
            launchImagePickerForAlbum(selectedAlbum)
        }
    })

    lateinit var facebookAlbumRecyclerView: RecyclerView
    var facebookDataManager : FacebookDataManager = FacebookDataManager

    fun bindView(recyclerView: RecyclerView) {
        facebookAlbumRecyclerView = recyclerView
        facebookAlbumRecyclerView.adapter = facebookAlbumAdapter
    }

    fun loadAlbums() {
        facebookDataManager.requestAlbums(getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ albumList ->
                    facebookAlbumAdapter.addAlbumList(albumList)
                }, { throwable ->

                })
    }

    fun getAccessToken() : AccessToken {
        return AccessToken.getCurrentAccessToken()
    }

    fun launchImagePickerForAlbum(selectedAlbum: FacebookAlbum) {
        val intent = Intent(activity, FacebookImagePickerActivity::class.java)
        intent.putExtra(FACEBOOK_ALBUM_ID_KEY, selectedAlbum.id)
        intent.putExtra(FACEBOOK_ALBUM_NAME_KEY, selectedAlbum.name)
        activity.startActivityForResult(intent, FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE)
    }
}