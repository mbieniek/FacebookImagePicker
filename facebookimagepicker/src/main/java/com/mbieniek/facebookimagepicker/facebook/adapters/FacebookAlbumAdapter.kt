package com.mbieniek.facebookimagepicker.facebook.adapters

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.mbieniek.facebookimagepicker.R
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings
import com.mbieniek.facebookimagepicker.facebook.data.FACEBOOK_PICTURE_URL
import com.mbieniek.facebookimagepicker.facebook.models.FacebookAlbum
import com.mbieniek.facebookimagepicker.facebook.util.inflate

import kotlinx.android.synthetic.main.item_facebook_album.view.*
import android.graphics.PorterDuff
import android.os.Build
import com.mbieniek.facebookimagepicker.facebook.util.loadImage

/**
 * Created by michaelbieniek on 3/17/18.
 */
class FacebookAlbumAdapter(val albumSelectedListener: AlbumSelectedListener) : RecyclerView.Adapter<FacebookAlbumAdapter.FacebookAlbumViewHolder>() {

    var albumList: List<FacebookAlbum> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacebookAlbumViewHolder {
        val inflatedView = parent.inflate(R.layout.item_facebook_album, false)
        return FacebookAlbumViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: FacebookAlbumViewHolder, position: Int) {
        holder.bind(albumList[position], albumSelectedListener)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    fun addAlbumList(albumList: List<FacebookAlbum>) {
        this.albumList = albumList
        notifyDataSetChanged()
    }

    class FacebookAlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(album: FacebookAlbum, albumSelectedListener: AlbumSelectedListener) {
            itemView.facebook_album_name.text = album.name
            itemView.facebook_album_photo_count.text = album.count.toString()
            val placeholderDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_collections_24dp)
            val color = Color.parseColor(FacebookImagePickerSettings.placeholderDrawableColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTint(placeholderDrawable!!, color)

            } else {
                placeholderDrawable!!.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }

            if (album.coverPhotoId != null) {
                val url = String.format(FACEBOOK_PICTURE_URL, album.coverPhotoId, AccessToken.getCurrentAccessToken().token)
                loadImage(itemView.context, url, itemView.facebook_album_avatar, placeholderDrawable)
            }
            itemView.setOnClickListener { _ ->
                albumSelectedListener.albumSelected(album)
            }
        }
    }

    interface AlbumSelectedListener {
        fun albumSelected(selectedAlbum: FacebookAlbum)
    }
}