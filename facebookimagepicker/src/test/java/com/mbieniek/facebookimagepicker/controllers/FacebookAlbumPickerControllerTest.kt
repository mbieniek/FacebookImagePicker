package com.mbieniek.facebookimagepicker.controllers

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.facebook.AccessToken
import com.facebook.BuildConfig
import com.mbieniek.facebookimagepicker.TestFacebookImagePickerApplication
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_ALBUM_ID_KEY
import com.mbieniek.facebookimagepicker.facebook.FACEBOOK_ALBUM_NAME_KEY
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerActivity
import com.mbieniek.facebookimagepicker.facebook.controllers.FacebookAlbumPickerController
import com.mbieniek.facebookimagepicker.facebook.data.FacebookDataManager
import com.mbieniek.facebookimagepicker.facebook.models.FacebookAlbum
import com.mbieniek.facebookimagepicker.util.DefaultConfig
import com.mbieniek.facebookimagepicker.util.RxSchedulersOverrideRule
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Robolectric
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.robolectric.Shadows


/**
 * Created by michaelbieniek on 3/30/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [DefaultConfig.EMULATE_SDK], application = TestFacebookImagePickerApplication::class)
class FacebookAlbumPickerControllerTest {

    @Rule
    @JvmField
    var rule = MockitoJUnit.rule()

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    lateinit var controller: FacebookAlbumPickerController
    var dataManagerMock : FacebookDataManager = Mockito.mock(FacebookDataManager::class.java)
    val activity = Robolectric.setupActivity(AppCompatActivity::class.java)

    @Before
    fun setUp() {
        controller = spy(FacebookAlbumPickerController(activity))
    }

    @Test
    fun testBindView() {
        val recyclerView = Mockito.mock(RecyclerView::class.java)
        controller.bindView(recyclerView)

        Assert.assertEquals(recyclerView, controller.facebookAlbumRecyclerView)
        Mockito.verify(recyclerView).setAdapter(controller.facebookAlbumAdapter)
    }

    @Test
    fun testLoadAlbums() {
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null, null)
        val resultList = ArrayList<FacebookAlbum>()
        resultList.add(FacebookAlbum(1, "Album 1", 22, null))
        resultList.add(FacebookAlbum(2, "Album 2", 33, null))
        controller.facebookDataManager = dataManagerMock
        Mockito.doReturn(accessToken).`when`(controller).getAccessToken()
        `when`(dataManagerMock.requestAlbums(accessToken)).thenReturn(Observable.just(resultList))

        controller.loadAlbums()

        assertEquals(2, controller.facebookAlbumAdapter.itemCount)
        assertEquals(resultList, controller.facebookAlbumAdapter.albumList)
    }

    @Test
    fun testLaunchImagePickerForAlbum() {
        val album = FacebookAlbum(123, "Test Album", 34, null)

        controller.launchImagePickerForAlbum(album)

        val shadowActivity = Shadows.shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivityForResult
        assertNotNull(startedIntent)
        assertEquals(FacebookImagePickerActivity::class.java.name, startedIntent.intent.component.className)
        assertEquals(123, startedIntent.intent.getLongExtra(FACEBOOK_ALBUM_ID_KEY, 0))
        assertEquals("Test Album", startedIntent.intent.getStringExtra(FACEBOOK_ALBUM_NAME_KEY))
    }
}