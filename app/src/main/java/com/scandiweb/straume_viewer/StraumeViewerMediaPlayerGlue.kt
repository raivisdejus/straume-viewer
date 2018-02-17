package com.scandiweb.straume_viewer

import android.content.Context
import android.support.v17.leanback.media.MediaPlayerGlue
import android.support.v17.leanback.widget.ArrayObjectAdapter

class StraumeViewerMediaPlayerGlue(context: Context) : MediaPlayerGlue(context) {
    override fun onCreateSecondaryActions(secondaryActionsAdapter: ArrayObjectAdapter?) {
        // Do not create secondary actions (replay, thumbs up, thumbs down)
    }

    override fun getMediaSubtitle(): CharSequence {
        // Hide "N/a" from the secondary title, as we do not have artist names
        return ""
    }
}
