package com.mbieniek.android.ui.facebook.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
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
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.item_facebook_album.view.*


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
            DrawableCompat.setTint(placeholderDrawable!!, Color.parseColor(FacebookImagePickerSettings.placeholderDrawableColor))

            if (album.coverPhotoId != null) {
                val url = String.format(FACEBOOK_PICTURE_URL, album.coverPhotoId, AccessToken.getCurrentAccessToken().token)
                Picasso.with(itemView.context)
                        .load(url)
                        .placeholder(placeholderDrawable)
                        .into(itemView.facebook_album_avatar)
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