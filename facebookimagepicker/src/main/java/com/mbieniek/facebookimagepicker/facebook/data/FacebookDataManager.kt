package com.mbieniek.facebookimagepicker.facebook.data

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.mbieniek.facebookimagepicker.facebook.models.FacebookAlbum
import com.mbieniek.facebookimagepicker.facebook.models.FacebookPicture
import io.reactivex.Observable
import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 * Created by michaelbieniek on 3/17/18.
 */

const val GRAPH_PATH_ALBUMS = "me/albums?fields=id,name,count,cover_photo"
const val GRAPH_PATH_PHOTOS = "/%d/photos?fields=picture,source,id"
const val FACEBOOK_PICTURE_URL = "https://graph.facebook.com/%d/picture?type=thumbnail&access_token=%s"
const val RESPONSE_JSON_NAME_DATA = "data"
const val RESPONSE_JSON_ALBUM_ID = "id"
const val RESPONSE_JSON_ALBUM_NAME = "name"
const val RESPONSE_JSON_ALBUM_COUNT = "count"
const val RESPONSE_JSON_COVER_PHOTO = "cover_photo"
const val RESPONSE_JSON_COVER_PHOTO_ID = "id"
const val RESPONSE_JSON_PICTURE_PREVIEW = "picture"
const val RESPONSE_JSON_PICTURE_SOURCE = "source"
const val RESPONSE_JSON_PICTURE_ID = "id"

object FacebookDataManager {

    fun requestPictures(albumId: Long, accessToken: AccessToken): Observable<List<FacebookPicture>> {
        val path = String.format(GRAPH_PATH_PHOTOS, albumId)
        val graphRequest = createGraphRequest(accessToken, path)
        return request(graphRequest).flatMap { response: GraphResponse ->
            Observable.just(convertJsonObjectToFacebookPictureList(response.jsonObject))
        }
    }

    fun convertJsonObjectToFacebookPictureList(dataJSON: JSONObject): List<FacebookPicture> {
        var pictureList: ArrayList<FacebookPicture> = ArrayList()
        val dataJSONArray = dataJSON.optJSONArray(RESPONSE_JSON_NAME_DATA)
        if (dataJSONArray != null) {
            for (index in 0..(dataJSONArray.length() - 1)) {
                val item = dataJSONArray.getJSONObject(index)
                pictureList.add(FacebookPicture(item.getString(RESPONSE_JSON_PICTURE_PREVIEW), item.getString(RESPONSE_JSON_PICTURE_SOURCE), item.getLong(RESPONSE_JSON_PICTURE_ID)))
            }
        }
        return pictureList
    }

    fun requestAlbums(accessToken: AccessToken): Observable<List<FacebookAlbum>> {
        val graphRequest = createGraphRequest(accessToken, GRAPH_PATH_ALBUMS)

        return request(graphRequest).flatMap { response: GraphResponse ->
            Observable.just(convertJsonObjectToFacebookAlbumList(response.jsonObject))
        }
    }

    fun convertJsonObjectToFacebookAlbumList(dataJSON: JSONObject): List<FacebookAlbum> {
        var albumList: ArrayList<FacebookAlbum> = ArrayList()
        val dataJSONArray = dataJSON.optJSONArray(RESPONSE_JSON_NAME_DATA)
        if (dataJSONArray != null) {
            for (index in 0..(dataJSONArray.length() - 1)) {
                val item = dataJSONArray.getJSONObject(index)
                if (item.has(RESPONSE_JSON_COVER_PHOTO)) {
                    val photo = item.getJSONObject(RESPONSE_JSON_COVER_PHOTO)
                    albumList.add(FacebookAlbum(item.getLong(RESPONSE_JSON_ALBUM_ID), item.getString(RESPONSE_JSON_ALBUM_NAME), item.getInt(RESPONSE_JSON_ALBUM_COUNT), photo.getLong(RESPONSE_JSON_COVER_PHOTO_ID)))
                } else {
                    albumList.add(FacebookAlbum(item.getLong(RESPONSE_JSON_ALBUM_ID), item.getString(RESPONSE_JSON_ALBUM_NAME), item.getInt(RESPONSE_JSON_ALBUM_COUNT), null))
                }
            }
        }
        return albumList
    }

     fun request(request: GraphRequest): Observable<GraphResponse> {
        return Observable.create { subscriber ->
            request.callback = GraphRequest.Callback {
                subscriber.onNext(it)
                subscriber.onComplete()
            }
            request.executeAndWait()
        }
    }

    fun createGraphRequest(accessToken: AccessToken, path: String): GraphRequest {
        return GraphRequest(accessToken, path)
    }
}