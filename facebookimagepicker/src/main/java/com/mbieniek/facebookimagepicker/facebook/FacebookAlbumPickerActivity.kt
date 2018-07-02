package com.mbieniek.facebookimagepicker.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.mbieniek.facebookimagepicker.R
import com.mbieniek.facebookimagepicker.facebook.controllers.FacebookAlbumPickerController
import com.mbieniek.facebookimagepicker.facebook.controllers.PERMISSION_USER_PHOTOS
import java.util.*

/**
 * Created by michaelbieniek on 3/17/18.
 */

const val FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE = 456

class FacebookAlbumPickerActivity : AppCompatActivity() {

    var controller : FacebookAlbumPickerController = FacebookAlbumPickerController(this)
    lateinit var callbackManager: CallbackManager
    lateinit var loginManager: LoginManager
    lateinit var albumRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_album_picker)
        setTitle(FacebookImagePickerSettings.albumActivityTitle)
        loginManager = LoginManager.getInstance()
        callbackManager = CallbackManager.Factory.create()
        albumRecyclerView = findViewById(R.id.facebook_image_picker_album_list)
        albumRecyclerView.layoutManager = LinearLayoutManager(this)
        controller.bindView(albumRecyclerView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        facebookLoginAndRequestPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { onBackPressed(); return true }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun facebookLoginAndRequestPermission() {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                controller.loadAlbums()
            }

            override fun onCancel() {
                finish()
            }

            override fun onError(error: FacebookException) {
                finish()
            }
        })
        // Not logged into Facebook
        if (AccessToken.getCurrentAccessToken() == null) {
            loginManager.logInWithReadPermissions(this, Arrays.asList(PERMISSION_USER_PHOTOS))
        } else {
            if (!checkUserPhotosPermission()) {
                loginManager.logInWithReadPermissions(this, Arrays.asList(PERMISSION_USER_PHOTOS))
            } else {
                controller.loadAlbums()
            }
        }
    }

    fun checkUserPhotosPermission() : Boolean {
        if (AccessToken.getCurrentAccessToken() != null && AccessToken.getCurrentAccessToken().permissions.contains(PERMISSION_USER_PHOTOS)) {
            return true
        }
        return false
    }


}