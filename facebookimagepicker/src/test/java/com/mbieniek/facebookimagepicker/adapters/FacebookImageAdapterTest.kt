package com.mbieniek.facebookimagepicker.adapters

import android.view.View
import android.widget.FrameLayout
import com.mbieniek.facebookimagepicker.BuildConfig
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings
import com.mbieniek.facebookimagepicker.facebook.adapters.FacebookImageAdapter
import com.mbieniek.facebookimagepicker.facebook.models.FacebookPicture
import com.mbieniek.facebookimagepicker.util.DefaultConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.item_facebook_image.view.*
import org.junit.Before
import org.robolectric.RuntimeEnvironment


/**
 * Created by michaelbieniek on 3/30/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [DefaultConfig.EMULATE_SDK])
class FacebookImageAdapterTest {

    lateinit var adapter: FacebookImageAdapter

    var imageSelectedListenerParam = 0

    @Before
    fun setUp() {
        imageSelectedListenerParam = 0
        adapter = FacebookImageAdapter( object : FacebookImageAdapter.ImageSelectedListener {
            override fun imageSelectedListUpdated(numberOfImagesSelected: Int) {
                imageSelectedListenerParam = numberOfImagesSelected
            }
        })
    }

    @Test
    fun testEmptyAdapter() {
        assertEquals(0, adapter.itemCount)
        assertEquals(0, adapter.selectedImageList.size)
    }

    @Test
    fun testAddImageList() {
        val facebookPictureList = ArrayList<FacebookPicture>()
        facebookPictureList.add(FacebookPicture("previewUrl", "sourceUrl", 123))
        facebookPictureList.add(FacebookPicture("previewUrl2", "sourceUrl2", 456))

        adapter.addImageList(facebookPictureList)

        assertEquals(2, adapter.itemCount)
        assertEquals(0, adapter.selectedImageList.size)
        assertEquals(0, imageSelectedListenerParam)
    }

    @Test
    fun testClickImage() {
        val parent = FrameLayout(RuntimeEnvironment.application)
        val holder = adapter.onCreateViewHolder(parent, 0)
        val facebookPictureList = ArrayList<FacebookPicture>()
        facebookPictureList.add(FacebookPicture("previewUrl", "sourceUrl", 123))
        facebookPictureList.add(FacebookPicture("previewUrl2", "sourceUrl2", 456))

        adapter.addImageList(facebookPictureList)
        adapter.onBindViewHolder(holder, 0)
        holder.itemView.performClick()

        assertEquals(2, adapter.itemCount)
        assertEquals(1, adapter.selectedImageList.size)
        assertEquals(123, adapter.selectedImageList[0].id)
        assertEquals(View.VISIBLE, holder.itemView.facebook_selected_check_image.visibility)
        assertEquals(1, imageSelectedListenerParam)
    }

    @Test
    fun testDeselectImage() {
        val parent = FrameLayout(RuntimeEnvironment.application)
        val holder = adapter.onCreateViewHolder(parent, 0)
        val facebookPictureList = ArrayList<FacebookPicture>()
        facebookPictureList.add(FacebookPicture("previewUrl", "sourceUrl", 123))
        facebookPictureList.add(FacebookPicture("previewUrl2", "sourceUrl2", 456))
        adapter.addImageList(facebookPictureList)
        adapter.onBindViewHolder(holder, 0)

        holder.itemView.performClick()
        holder.itemView.performClick()

        assertEquals(2, adapter.itemCount)
        assertEquals(0, adapter.selectedImageList.size)
        assertEquals(View.INVISIBLE, holder.itemView.facebook_selected_check_image.visibility)
        assertEquals(0, imageSelectedListenerParam)
    }

    @Test
    fun testMaximumSelectedImages() {
        FacebookImagePickerSettings.maximumSelectableImages = 1
        val parent = FrameLayout(RuntimeEnvironment.application)
        val holder1 = adapter.onCreateViewHolder(parent, 0)
        val holder2 = adapter.onCreateViewHolder(parent, 0)
        val facebookPictureList = ArrayList<FacebookPicture>()
        facebookPictureList.add(FacebookPicture("previewUrl", "sourceUrl", 123))
        facebookPictureList.add(FacebookPicture("previewUrl2", "sourceUrl2", 456))
        adapter.addImageList(facebookPictureList)
        adapter.onBindViewHolder(holder1, 0)
        adapter.onBindViewHolder(holder2, 1)

        holder2.itemView.performClick()
        holder1.itemView.performClick()

        assertEquals(1, adapter.selectedImageList.size)
        assertEquals(456, adapter.selectedImageList[0].id)
        assertEquals(View.INVISIBLE, holder1.itemView.facebook_selected_check_image.visibility)
        assertEquals(View.VISIBLE, holder2.itemView.facebook_selected_check_image.visibility)
        assertEquals(1, imageSelectedListenerParam)
    }
}