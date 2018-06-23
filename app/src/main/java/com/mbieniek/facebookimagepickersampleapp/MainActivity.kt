package com.mbieniek.facebookimagepickersampleapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_IMAGE_RESULT_KEY
import com.mbieniek.facebookimagepicker.facebook.FacebookAlbumPickerActivity
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings
import com.mbieniek.facebookimagepicker.facebook.models.FacebookPicture
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


class MainActivity : AppCompatActivity() {

    val dataManager : DataManager = DataManager
    lateinit var carouselView: CarouselView
    var downloadedImageList : ArrayList<File> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataManager.path = applicationContext.filesDir.path
        setContentView(R.layout.activity_main)

        carouselView = findViewById(R.id.carouselView)
        var imageListener = ImageListener { position, imageView ->
            if (position < downloadedImageList.size) {
                val bitmap = BitmapFactory.decodeFile(downloadedImageList.get(position).absolutePath)
                imageView.setImageBitmap(bitmap)
            }
        }
        carouselView.setImageListener(imageListener)

        val button : Button = findViewById(R.id.button)
        button.setOnClickListener({
            val intent = Intent(this, FacebookAlbumPickerActivity::class.java)
            startActivityForResult(intent, FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE)
        })
        FacebookImagePickerSettings.placeholderDrawableColor = "#3F51B5"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val rawImages = data.getParcelableArrayExtra(FACEBOOK_IMAGE_RESULT_KEY)
            if (rawImages != null && rawImages.size > 0) {
                for (i in rawImages.indices) {
                    //Here you have access to the picture url to do as you please (either download the picture to device
                    //or pass the url onto another app/library. For an example, we'll download and display the images
                    //in a carousel view.
                    val picture = rawImages[i] as FacebookPicture

                    dataManager.downloadFileByUrlToFileDir(picture.sourceUrl)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                if (it != null) {
                                    downloadedImageList.add(it)
                                    carouselView.pageCount = downloadedImageList.size
                                    Toast.makeText(this, "Image #" + i + " downloaded successfully!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this, "Image #" + i + " failed to download!", Toast.LENGTH_LONG).show()
                                }
                            })
                }
            }
        }
    }
}
