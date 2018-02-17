package com.scandiweb.straume_viewer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v17.leanback.app.BackgroundManager
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget


class PlaylistFragment : VerticalGridFragment() {
    companion object {
        val NUM_COLUMNS = 5
    }

    lateinit var playlist : VideoItem

    lateinit var mBackgroundManager: BackgroundManager
    lateinit var mDefaultBackground: Drawable
    lateinit var  mMetrics : DisplayMetrics

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

    private fun setupFragment() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        // loaderManager.initLoader(ALL_VIDEOS_LOADER, null, this)
        // After 500ms, start the animation to transition the cards into view.
        Handler().postDelayed({ startEntranceTransition() }, 500)
        onItemViewClickedListener = ItemViewClickedListener()
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

    /*
    fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        return CursorLoader(
                activity,
                VideoContract.VideoEntry.CONTENT_URI, // selection clause
                null  // sort order
                , null, null, null
        )// projection
        // selection
    }
    */

    /*
    fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (loader.getId() === ALL_VIDEOS_LOADER && cursor != null && cursor!!.moveToFirst()) {
            mVideoCursorAdapter.changeCursor(cursor)
        }
    }
    */

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override  fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any,
                                    rowViewHolder: RowPresenter.ViewHolder, row: Row) {


            // TODO Launch the playlist inside the video
//            if (item is Video) {
//                val video = item as Video
//
//                val intent = Intent(activity, VideoDetailsActivity::class.java)
//                intent.putExtra(VideoDetailsActivity.VIDEO, video)
//
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity,
//                        (itemViewHolder.view as ImageCardView).mainImageView,
//                        VideoDetailsActivity.SHARED_ELEMENT_NAME).toBundle()
//                activity.startActivity(intent, bundle)
        }
    }
}


