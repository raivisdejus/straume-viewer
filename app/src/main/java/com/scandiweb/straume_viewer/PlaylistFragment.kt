package com.scandiweb.straume_viewer

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v17.leanback.app.BackgroundManager
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.OnItemViewClickedListener
import android.support.v17.leanback.widget.VerticalGridPresenter
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.scandiweb.straume_viewer.Api.StraumeService
import com.scandiweb.straume_viewer.Model.PlayListVideo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PlaylistFragment : VerticalGridFragment() {
    companion object {
        val NUM_COLUMNS = 5
    }
    lateinit var playlist : VideoItem
    lateinit var mBackgroundManager: BackgroundManager
    lateinit var mDefaultBackground: Drawable
    lateinit var  mMetrics : DisplayMetrics
    var selectedItem : VideoItem? = null

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(activity.window)
        mDefaultBackground = ContextCompat.getDrawable(activity, R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = activity?.intent?.getSerializableExtra(PlaylistActivity.PLAYLIST) as VideoItem
        this.title = playlist.title

        prepareBackgroundManager()
        updateBackground(playlist.backgroundImageUrl)
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        setupFragment()
    }


    override fun onResume() {
        super.onResume()
        Log.d("Playlist", "selected item $selectedItem")
        selectedItem ?.let {
         updateBackground(it.backgroundImageUrl)
        }
    }
    private fun setupFragment() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        adapter = ArrayObjectAdapter(CardPresenter())
        Handler().postDelayed({ startEntranceTransition() }, 500)
        StraumeService.create(context)
            .getVideosFromPlaylist(playlist.videoUrl !!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                videoPage ->
                    showVideos(videos = videoPage.videos)

            })

        onItemViewClickedListener = OnItemViewClickedListener({ _, item, _, _ ->
            val intent :Intent?
            item as VideoItem
            selectedItem = item
            intent = Intent(activity, PlaybackActivity::class.java)
            intent.putExtra(DetailsActivity.MOVIE, item)
            startActivity(intent)
        })

        setOnItemViewSelectedListener({_, item, _, _ ->
            item?.let{
                updateBackground((item as VideoItem).backgroundImageUrl)
            }
        })
    }

    private fun showVideos(videos: List<PlayListVideo>?) {
        videos?.forEach {
            val videoItem = VideoItem()
            // videoItem.id = listRowAdapter.size().toLong()
            videoItem.title = it.title
            videoItem.cardImageUrl = it.imageUrl
            videoItem.backgroundImageUrl = it.imageUrl
            videoItem.videoUrl = it.url
            videoItem.isPlaylist = it.isPlaylist
            (adapter as ArrayObjectAdapter).add(videoItem)
            adapter.notifyItemRangeChanged(adapter.size(), 1)
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(activity)
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into<SimpleTarget<GlideDrawable>>(
                        object : SimpleTarget<GlideDrawable>(width, height) {
                            override fun onResourceReady(resource: GlideDrawable,
                                                         glideAnimation: GlideAnimation<in GlideDrawable>) {
                                mBackgroundManager.drawable = resource
                            }
                        })
    }

}


