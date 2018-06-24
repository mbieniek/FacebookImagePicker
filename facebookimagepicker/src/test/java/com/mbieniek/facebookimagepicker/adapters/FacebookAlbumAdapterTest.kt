package com.mbieniek.facebookimagepicker.adapters

import android.widget.FrameLayout
import com.mbieniek.facebookimagepicker.BuildConfig
import com.mbieniek.facebookimagepicker.facebook.adapters.FacebookAlbumAdapter
import com.mbieniek.facebookimagepicker.facebook.models.FacebookAlbum
import com.mbieniek.facebookimagepicker.util.DefaultConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.item_facebook_album.view.*
import org.robolectric.RuntimeEnvironment


/**
 * Created by michaelbieniek on 3/30/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [DefaultConfig.EMULATE_SDK])
class FacebookAlbumAdapterTest {

    lateinit var adapter: FacebookAlbumAdapter
    var albumSelectedListenerParam : FacebookAlbum? = null

    @Before
    fun setUp() {
        adapter = FacebookAlbumAdapter( object : FacebookAlbumAdapter.AlbumSelectedListener {
            override fun albumSelected(selectedAlbum: FacebookAlbum) {
                albumSelectedListenerParam = selectedAlbum
            }
        })
    }

    @Test
    fun testEmptyAdapter() {
        assertEquals(0, adapter.itemCount)
        assertEquals(true, adapter.albumList.isEmpty())
    }

    @Test
    fun testAddAlbumList() {
        val facebookAlbumList = ArrayList<FacebookAlbum>()
        facebookAlbumList.add(FacebookAlbum(123, "First Album", 12, 321))
        facebookAlbumList.add(FacebookAlbum(456, "Second Album", 54, 654))

        adapter.addAlbumList(facebookAlbumList)

        assertEquals(2, adapter.itemCount)
        assertEquals(null, albumSelectedListenerParam)
        assertEquals(facebookAlbumList, adapter.albumList)
    }

    @Test
    fun testClickAlbum() {
        val parent = FrameLayout(RuntimeEnvironment.application)
        val holder = adapter.onCreateViewHolder(parent, 0)
        val facebookAlbumList = ArrayList<FacebookAlbum>()
        facebookAlbumList.add(FacebookAlbum(123, "First Album", 12, null))
        facebookAlbumList.add(FacebookAlbum(456, "Second Album", 54, null))
        adapter.addAlbumList(facebookAlbumList)
        adapter.onBindViewHolder(holder, 0)

        holder.itemView.performClick()

        assertEquals("First Album", holder.itemView.facebook_album_name.text)
        assertEquals("12", holder.itemView.facebook_album_photo_count.text)
        assertEquals(albumSelectedListenerParam, adapter.albumList[0])
    }
}