package com.scandiweb.straume_viewer.Model

import com.scandiweb.straume_viewer.Api.VideoImageUrlConverter
import com.scandiweb.straume_viewer.Api.VideoTitleConverter
import pl.droidsonroids.jspoon.annotation.Selector

class Video {
    @Selector(value = ".video-title", converter = VideoTitleConverter::class)
    var title: String? = null

    @Selector(value = "a", attr = "href")
    var url: String? = null
    @Selector(value = "img", attr = "srcset", converter = VideoImageUrlConverter::class)
    private var rawImageUrl: String? = null
    var imageUrl: String? = null
        get() {
            this.rawImageUrl?.let { return "http:${this.rawImageUrl}" }
            return null
        }

    @Selector(".playlist-overlay")
    var videosInPlaylist: String? = null

    var isPlaylist: Boolean = false
        get() = this.videosInPlaylist !== null

}
