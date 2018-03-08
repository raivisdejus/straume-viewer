package com.scandiweb.straume_viewer

import android.os.Bundle
import android.support.v4.app.FragmentActivity


class PlaybackActivity : FragmentActivity() {
//    private var playbackFragment: PlaybackFragment? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)
//        val fragment = fragmentManager.findFragmentByTag(getString(R.string.playback_tag))
//        if (fragment is PlaybackFragment) {
//            playbackFragment = fragment
//        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
