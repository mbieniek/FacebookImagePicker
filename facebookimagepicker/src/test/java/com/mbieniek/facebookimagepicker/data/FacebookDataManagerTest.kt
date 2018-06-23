package com.mbieniek.facebookimagepicker.data

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.mbieniek.facebookimagepicker.BuildConfig
import com.mbieniek.facebookimagepicker.facebook.data.FacebookDataManager
import com.mbieniek.facebookimagepicker.util.DefaultConfig
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Before

/**
 * Created by michaelbieniek on 3/30/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [DefaultConfig.EMULATE_SDK])
class FacebookDataManagerTest {

    lateinit var facebookDataManager: FacebookDataManager

    @Before
    fun setUp() {
        facebookDataManager = spy(FacebookDataManager)
    }

    @Test
    fun testConvertJsonArrayToFacebookAlbumListEmpty() {
        var jsonObject = JSONObject()

        val result = facebookDataManager.convertJsonObjectToFacebookAlbumList(jsonObject)
        assertEquals(0, result.size)
    }

    @Test
    fun testConvertJsonArrayToFacebookAlbumList() {
        val jsonObject = JSONObject("{\"data\":[{\"id\":\"488116813573\",\"name\":\"Profile Pictures\",\"count\":39,\"cover_photo\":{\"created_time\":\"2017-04-13T01:32:57+0000\",\"id\":\"10156025354733574\"}},{\"id\":\"10154643267293574\",\"name\":\"Europe 2016\",\"count\":54},{\"id\":\"10154643243118574\",\"name\":\"Forgotten Asia Trip Pics\",\"count\":38,\"cover_photo\":{\"created_time\":\"2016-03-16T22:33:20+0000\",\"name\":\"Pretty late warning considering that your already on the plane once it's moving when you get this...\",\"id\":\"10154643244548574\"}}]}")

        val result = facebookDataManager.convertJsonObjectToFacebookAlbumList(jsonObject)
        assertEquals(3, result.size)
        val firstAlbumWithCoverPhoto = result[0]
        assertEquals(488116813573, firstAlbumWithCoverPhoto.id)
        assertEquals("Profile Pictures", firstAlbumWithCoverPhoto.name)
        assertEquals(39, firstAlbumWithCoverPhoto.count)
        assertEquals(10156025354733574, firstAlbumWithCoverPhoto.coverPhotoId)
        val secondAlbumWithoutCoverPhoto = result[1]
        assertEquals(10154643267293574, secondAlbumWithoutCoverPhoto.id)
        assertEquals("Europe 2016", secondAlbumWithoutCoverPhoto.name)
        assertEquals(54, secondAlbumWithoutCoverPhoto.count)
        assertEquals(null, secondAlbumWithoutCoverPhoto.coverPhotoId)
        val thirdAlbumWithCoverPhoto = result[2]
        assertEquals(10154643243118574, thirdAlbumWithCoverPhoto.id)
        assertEquals("Forgotten Asia Trip Pics", thirdAlbumWithCoverPhoto.name)
        assertEquals(38, thirdAlbumWithCoverPhoto.count)
        assertEquals(10154643244548574, thirdAlbumWithCoverPhoto.coverPhotoId)
    }

    @Test
    fun testRequestAlbums() {
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null)
        val mockGraphRequest = mock(GraphRequest::class.java)
        val mockGraphResponse = mock(GraphResponse::class.java)
        `when`(facebookDataManager.createGraphRequest(accessToken, "me/albums?fields=id,name,count,cover_photo")).thenReturn(mockGraphRequest)
        `when`(facebookDataManager.request(mockGraphRequest)).thenReturn(Observable.just(mockGraphResponse))

        facebookDataManager.requestAlbums(accessToken)

        verify(facebookDataManager).createGraphRequest(accessToken, "me/albums?fields=id,name,count,cover_photo")
    }

    @Test
    fun testConvertJsonArrayToFacebookPictureListEmpty() {
        var jsonObject = JSONObject()

        val result = facebookDataManager.convertJsonObjectToFacebookPictureList(jsonObject)
        assertEquals(0, result.size)
    }

    @Test
    fun testConvertJsonArrayToFacebookPictureList() {
        val jsonObject = JSONObject("{\"data\":[{\"picture\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/s130x130\\/270092_10151536290508574_1139628428_n.jpg?_nc_cat=0&oh=d1e780df03551c4325e92ad43322c74d&oe=5B70EFF4\",\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-9\\/270092_10151536290508574_1139628428_n.jpg?_nc_cat=0&oh=1b64fe3b87628c658ce43201b916c2ed&oe=5B3B6822\",\"id\":\"10151536290508574\"},{\"picture\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/s130x130\\/551905_10151411162448574_810155800_n.jpg?_nc_cat=0&oh=5ee4ba31a68a12d5021f6a59d03fb0f3&oe=5B291F0C\",\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-9\\/551905_10151411162448574_810155800_n.jpg?_nc_cat=0&oh=8fa4515af4bcbd10e1b1d2dd7e99a2fb&oe=5B6B5DEA\",\"id\":\"10151411162448574\"}]}")

        val result = facebookDataManager.convertJsonObjectToFacebookPictureList(jsonObject)
        assertEquals(2, result.size)
        val firstPicture = result[0]
        assertEquals(10151536290508574, firstPicture.id)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-0/s130x130/270092_10151536290508574_1139628428_n.jpg?_nc_cat=0&oh=d1e780df03551c4325e92ad43322c74d&oe=5B70EFF4", firstPicture.previewUrl)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-9/270092_10151536290508574_1139628428_n.jpg?_nc_cat=0&oh=1b64fe3b87628c658ce43201b916c2ed&oe=5B3B6822", firstPicture.sourceUrl)
        val secondPicture = result[1]
        assertEquals(10151411162448574, secondPicture.id)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-0/s130x130/551905_10151411162448574_810155800_n.jpg?_nc_cat=0&oh=5ee4ba31a68a12d5021f6a59d03fb0f3&oe=5B291F0C", secondPicture.previewUrl)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-9/551905_10151411162448574_810155800_n.jpg?_nc_cat=0&oh=8fa4515af4bcbd10e1b1d2dd7e99a2fb&oe=5B6B5DEA", secondPicture.sourceUrl)
    }

    @Test
    fun testRequestPictures() {
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null)
        val mockGraphRequest = mock(GraphRequest::class.java)
        val mockGraphResponse = mock(GraphResponse::class.java)
        `when`(facebookDataManager.createGraphRequest(accessToken, "/123/photos?fields=picture,source,id")).thenReturn(mockGraphRequest)
        `when`(facebookDataManager.request(mockGraphRequest)).thenReturn(Observable.just(mockGraphResponse))

        facebookDataManager.requestPictures(123, accessToken)

        verify(facebookDataManager).createGraphRequest(accessToken, "/123/photos?fields=picture,source,id")
    }

}