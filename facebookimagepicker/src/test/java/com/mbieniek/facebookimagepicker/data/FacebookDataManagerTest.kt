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
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null, null)
        val mockGraphRequest = mock(GraphRequest::class.java)
        val mockGraphResponse = mock(GraphResponse::class.java)
        `when`(facebookDataManager.createGraphRequest(accessToken, "me/albums?fields=id,name,count,cover_photo")).thenReturn(mockGraphRequest)
        `when`(facebookDataManager.request(mockGraphRequest)).thenReturn(Observable.just(mockGraphResponse))

        facebookDataManager.requestAlbums(accessToken)

        verify(facebookDataManager).createGraphRequest(accessToken, "me/albums", "id","name","count","cover_photo")
    }

    @Test
    fun testConvertJsonArrayToFacebookPictureListEmpty() {
        var jsonObject = JSONObject()

        val result = facebookDataManager.convertJsonObjectToFacebookPictureList(jsonObject)
        assertEquals(0, result.size)
    }

    @Test
    fun testConvertJsonArrayToFacebookPictureList() {
        val jsonObject = JSONObject("{\"data\":[{\"picture\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/s130x130\\/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=4801591588d7c661abcd7943ed9a5eb3&oe=5C0E8844\",\"id\":\"10157517343193574\",\"images\":[{\"height\":473,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-9\\/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=a1c00fe40697274679737e985d181a06&oe=5BC627A2\",\"width\":465},{\"height\":325,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p320x320\\/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=5cb12f626fa2f66036e8f032802e8214&oe=5BDD4E15\",\"width\":320},{\"height\":132,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p130x130\\/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=37445ae7781317cb707f5552645f535f&oe=5BD36605\",\"width\":130},{\"height\":225,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p75x225\\/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=06d147fe2787f7c62fe46be5ce18d792&oe=5BC880B3\",\"width\":221}]},{\"picture\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p130x130\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=630292bdcb7186c5adc33ed7c06b57f5&oe=5BD456C5\",\"id\":\"10157369563553574\",\"images\":[{\"height\":1440,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t31.0-8\\/30814079_10157369563553574_6961709195396768105_o.jpg?_nc_cat=0&oh=32e26e1e19f4f5d6e0db5ef3b30b1d98&oe=5BD79CBA\",\"width\":1440},{\"height\":960,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-9\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=b01dcc645d4120d51d57e1b18e9e4a59&oe=5BCF3C62\",\"width\":960},{\"height\":720,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t31.0-8\\/p720x720\\/30814079_10157369563553574_6961709195396768105_o.jpg?_nc_cat=0&oh=89c5843eab6bdfc0b9a5bd942c2900e8&oe=5BCBBB29\",\"width\":720},{\"height\":600,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p600x600\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=f49af340a82438c0a24828a3b21772bb&oe=5C0619AD\",\"width\":600},{\"height\":480,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p480x480\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=00f66ad28a425fd9bafda94a5424c4a1&oe=5BD4A197\",\"width\":480},{\"height\":320,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p320x320\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=1bea5d3d1c4c654d2e34c462816836f2&oe=5C077FD5\",\"width\":320},{\"height\":540,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p180x540\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=e0121e2a22bf8881473241d318b1fc0b&oe=5BC944F0\",\"width\":540},{\"height\":130,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p130x130\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=630292bdcb7186c5adc33ed7c06b57f5&oe=5BD456C5\",\"width\":130},{\"height\":225,\"source\":\"https:\\/\\/scontent.xx.fbcdn.net\\/v\\/t1.0-0\\/p75x225\\/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=36dfd50fc3d77e265bfe2ef3baa4343d&oe=5C08EE73\",\"width\":225}]}]}")

        val result = facebookDataManager.convertJsonObjectToFacebookPictureList(jsonObject)
        assertEquals(2, result.size)
        val firstPicture = result[0]
        assertEquals(10157517343193574, firstPicture.id)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-0/s130x130/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=4801591588d7c661abcd7943ed9a5eb3&oe=5C0E8844", firstPicture.previewUrl)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-9/35544794_10157517343198574_3735050461368025088_n.jpg?_nc_cat=0&oh=a1c00fe40697274679737e985d181a06&oe=5BC627A2", firstPicture.sourceUrl)
        val secondPicture = result[1]
        assertEquals(10157369563553574, secondPicture.id)
        assertEquals("https://scontent.xx.fbcdn.net/v/t1.0-0/p130x130/31395126_10157369563553574_6961709195396768105_n.jpg?_nc_cat=0&oh=630292bdcb7186c5adc33ed7c06b57f5&oe=5BD456C5", secondPicture.previewUrl)
        assertEquals("https://scontent.xx.fbcdn.net/v/t31.0-8/30814079_10157369563553574_6961709195396768105_o.jpg?_nc_cat=0&oh=32e26e1e19f4f5d6e0db5ef3b30b1d98&oe=5BD79CBA", secondPicture.sourceUrl)
    }

    @Test
    fun testRequestPictures() {
        val accessToken = AccessToken("test", "test", "test", null, null, null, null, null, null)
        val mockGraphRequest = mock(GraphRequest::class.java)
        val mockGraphResponse = mock(GraphResponse::class.java)
        `when`(facebookDataManager.createGraphRequest(accessToken, "/123/photos?fields=picture,id,images")).thenReturn(mockGraphRequest)
        `when`(facebookDataManager.request(mockGraphRequest)).thenReturn(Observable.just(mockGraphResponse))

        facebookDataManager.requestPictures(123, accessToken)

        verify(facebookDataManager).createGraphRequest(accessToken, "/123/photos", "picture", "id", "images")
    }

}