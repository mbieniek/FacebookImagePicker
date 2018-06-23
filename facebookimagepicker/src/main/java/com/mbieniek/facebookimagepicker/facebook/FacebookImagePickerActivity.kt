package com.mbieniek.facebookimagepicker.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.mbieniek.facebookimagepicker.R
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings.imageGridSpanCount
import com.mbieniek.facebookimagepicker.facebook.adapters.FacebookImageAdapter
import com.mbieniek.facebookimagepicker.facebook.controllers.FacebookImagePickerController

/**
 * Created by michaelbieniek on 3/18/18.
 */

const val FACEBOOK_ALBUM_ID_KEY = "facebookImagePickerAlbumId"
const val FACEBOOK_ALBUM_NAME_KEY = "facebookImagePickerAlbumName"
const val FACEBOOK_IMAGE_RESULT_KEY = "facebookImageResults"

class FacebookImagePickerActivity : AppCompatActivity() {

    lateinit var controller : FacebookImagePickerController
    lateinit var imageRecyclerView: RecyclerView
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_image_picker)
        title = intent.getStringExtra(FACEBOOK_ALBUM_NAME_KEY)
        controller = FacebookImagePickerController(
                intent.getLongExtra(FACEBOOK_ALBUM_ID_KEY, -1),
                object : FacebookImageAdapter.ImageSelectedListener {
                    override fun imageSelectedListUpdated(numberOfImagesSelected: Int) {
                        val menuItem = menu.findItem(R.id.menu_facebook_images_selected)
                        if (numberOfImagesSelected > 0) {
                            menuItem.title = String.format(FacebookImagePickerSettings.imagesSelectedText, numberOfImagesSelected)
                            menuItem.isVisible = true
                        } else {
                            menuItem.isVisible = false
                        }
                    }
        })
        imageRecyclerView = findViewById(R.id.facebook_image_gridview)
        imageRecyclerView.layoutManager = GridLayoutManager(this, imageGridSpanCount)
        controller.bindView(imageRecyclerView)
        controller.loadPictures()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_facebook_image_picker, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_facebook_images_selected -> {
                setResultAndFinish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setResultAndFinish() {
        intent = Intent()
        intent.putExtra(FACEBOOK_IMAGE_RESULT_KEY, controller.getSelectedFacebookPictures().toTypedArray())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}