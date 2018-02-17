package com.scandiweb.straume_viewer

import android.app.Activity
import android.os.Bundle

class PlaylistActivity: Activity() {

    companion object {
        val PLAYLIST = "playlist"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
    }
}