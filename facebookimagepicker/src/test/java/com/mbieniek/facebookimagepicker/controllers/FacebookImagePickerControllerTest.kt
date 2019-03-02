package com.mbieniek.facebookimagepicker.controllers

import android.support.v7.widget.RecyclerView
import com.facebook.AccessToken
import com.mbieniek.facebookimagepicker.BuildConfig
import com.mbieniek.facebookimagepicker.TestFacebookImagePickerApplication
import com.mbieniek.facebookimagepicker.facebook.adapters.FacebookImageAdapter
import com.mbieniek.facebookimagepicker.facebook.controllers.FacebookImagePickerController
import com.mbieniek.facebookimagepicker.facebook.data.FacebookDataManager
import com.mbieniek.facebookimagepicker.facebook.models.FacebookPicture
import com.mbieniek.facebookimagepicker.util.DefaultConfig
import com.mbieniek.facebookimagepicker.util.RxSchedulersOverrideRule
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit


/**
 * Created by michaelbieniek on 3/30/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [DefaultConfig.EMULATE_SDK], application = TestFacebookImagePickerApplication::class)
class FacebookImagePickerControllerTest {

    @Rule @JvmField
    var rule = MockitoJUnit.rule()

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    lateinit var controller: FacebookImagePickerController
    var dataManagerMock : FacebookDataManager = mock(FacebookDataManager::class.java)
    var imageSelectedListenerParam = 0

    @Before
    fun setUp() {
        controller = spy(FacebookImagePickerController(123, object : FacebookImageAdapter.ImageSelectedListener {
            override fun imageSelectedListUpdated(numberOfImagesSelected: Int) {
                imageSelectedListenerParam = numberOfImagesSelected
            }
        }))
    }

    @Test
    fun testBindView() {
        val recyclerView = mock(RecyclerView::class.java)
        controller.bindView(recyclerView)

        assertEquals(recyclerView, controller.facebookImageRecyclerView)
        verify(recyclerView).setAdapter(controller.facebookImageAdapter)
    }

    @Test
    fun testLoadPictures() {
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null, null)
        val resultList = ArrayList<FacebookPicture>()
        resultList.add(FacebookPicture("previewUrl", "sourceUrl", 123))
        resultList.add(FacebookPicture("previewUrl2", "sourceUrl2", 456))
        controller.facebookDataManager = dataManagerMock
        doReturn(accessToken).`when`(controller).getAccessToken()
        `when`(dataManagerMock.requestPictures(123, accessToken)).thenReturn(Observable.just(resultList))

        controller.loadPictures()

        assertEquals(2, controller.facebookImageAdapter.itemCount)
        assertEquals(true, controller.facebookImageAdapter.selectedImageList.isEmpty())
    }
}