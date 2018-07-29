package com.mbieniek.facebookimagepicker.facebook.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mbieniek.facebookimagepicker.R
import com.mbieniek.facebookimagepicker.facebook.FacebookImagePickerSettings
import com.mbieniek.facebookimagepicker.facebook.models.FacebookPicture
import com.mbieniek.facebookimagepicker.facebook.util.inflate
import com.mbieniek.facebookimagepicker.facebook.util.loadImage
import kotlinx.android.synthetic.main.item_facebook_image.view.*


/**
 * Created by michaelbieniek on 3/18/18.
 */
class FacebookImageAdapter(val imageSelectedListener: ImageSelectedListener) : RecyclerView.Adapter<FacebookImageAdapter.FacebookImageViewHolder>() {

    private var imageList : List<FacebookPicture> = ArrayList()
    var selectedImageList : ArrayList<FacebookPicture> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacebookImageViewHolder {
        val inflatedView = parent.inflate(R.layout.item_facebook_image, false)
        return FacebookImageViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: FacebookImageViewHolder, position: Int) {
        val facebookPicture = imageList[position]
        holder.bind(facebookPicture, object : FacebookImageViewHolder.ImageClickListener {
            override fun imageClicked(itemView: View, image: FacebookPicture) {
                if (selectedImageList.contains(image)) {
                    selectedImageList.remove(image)
                    itemView.facebook_selected_check_image.visibility = View.INVISIBLE
                } else {
                    if (selectedImageList.size < FacebookImagePickerSettings.maximumSelectableImages) {
                        selectedImageList.add(image)
                        itemView.facebook_selected_check_image.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(itemView.context, FacebookImagePickerSettings.maximumImagesSelectedText, Toast.LENGTH_LONG).show()
                    }
                }
                imageSelectedListener.imageSelectedListUpdated(selectedImageList.size)
            }
        }, selectedImageList.contains(facebookPicture))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun addImageList(imageList: List<FacebookPicture>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }

    class FacebookImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: FacebookPicture, imageClickListener: ImageClickListener, isSelected: Boolean) {
            loadImage(itemView.context, image.previewUrl, itemView.facebook_picture_image)

            itemView.setOnClickListener { v ->
                imageClickListener.imageClicked(v, image)
            }

            itemView.facebook_selected_check_image.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
        }

        interface ImageClickListener {
            fun imageClicked(itemView: View, image: FacebookPicture)
        }
    }

    interface ImageSelectedListener {
        fun imageSelectedListUpdated(numberOfImagesSelected: Int)
    }
}