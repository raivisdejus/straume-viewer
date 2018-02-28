package com.scandiweb.straume_viewer

import android.net.Uri
import android.os.Bundle
import android.support.v17.leanback.app.VideoFragment
import android.support.v17.leanback.app.VideoFragmentGlueHost
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.scandiweb.straume_viewer.Api.StraumeService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/** Handles video playback with media controls. */
class PlaybackFragment : VideoFragment() {

    private val UPDATE_DELAY = 16
    private var mPlayer: SimpleExoPlayer? = null
    private var videoItem: VideoItem? = null
    private var mPlayerGlue: VideoPlayerGlue? = null
    private var mPlayerAdapter: LeanbackPlayerAdapter? = null
    private var mTrackSelector: TrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Video item is the item in category pages, to get video details we use  StraumeService.getVideoDetails()
        videoItem = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as VideoItem


        StraumeService.create(context!!)
                .getVideoDetails(videoItem?.videoUrl!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ video ->
                    mPlayerGlue?.title = video.title
                    prepareMediaForPlaying(Uri.parse(video.videoURL))
                    mPlayerGlue?.play()
                })
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun prepareMediaForPlaying(mediaSourceUri: Uri) {
        val userAgent = Util.getUserAgent(activity, "VideoPlayerGlue")
        val mediaSource = ExtractorMediaSource(
                mediaSourceUri,
                DefaultDataSourceFactory(activity, userAgent),
                DefaultExtractorsFactory(),
                null, null)

        mPlayer?.prepare(mediaSource)
    }


    private fun initializePlayer() {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        mTrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        mPlayer = ExoPlayerFactory.newSimpleInstance(activity, mTrackSelector)
        mPlayerAdapter = LeanbackPlayerAdapter(activity, mPlayer, UPDATE_DELAY)
        mPlayerGlue = VideoPlayerGlue(activity, mPlayerAdapter)
        mPlayerGlue?.host = VideoFragmentGlueHost(this)
        mPlayerGlue?.playWhenPrepared()
    }

    private fun releasePlayer() {
        if (mPlayer != null) {
            mPlayer?.release()
            mPlayer = null
            mTrackSelector = null
            mPlayerGlue = null
            mPlayerAdapter = null
        }
    }
}