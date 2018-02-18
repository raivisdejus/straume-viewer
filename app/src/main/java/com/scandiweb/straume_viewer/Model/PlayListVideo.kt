package com.scandiweb.straume_viewer.Model

import com.scandiweb.straume_viewer.Api.VideoTitleConverter
import pl.droidsonroids.jspoon.annotation.Selector

class PlayListVideo {

    @Selector(value = ".video-title", converter = VideoTitleConverter::class)
    var rawTitle: String? = null

    var title: String? = null
        get() {
            if (this.videosInPlaylist !== null){
                return "${this.rawTitle} (${this.videosInPlaylist})"
            } else {
                return this.rawTitle
            }
        }

    @Selector(value = "a", attr = "href")
    var url: String? = null

    @Selector(value = "img", attr = "src")
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

    override fun toString(): String {
        return "PlayListVideo(rawTitle=$rawTitle, url=$url, rawImageUrl=$rawImageUrl, videosInPlaylist=$videosInPlaylist)"
    }

}
