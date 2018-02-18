package com.scandiweb.straume_viewer.Model

import pl.droidsonroids.jspoon.annotation.Selector

class PlayListVideoPage {
    @Selector("#video-heading h1")
    var title: String? = null

    @Selector(".video-item")
    var videos: List<PlayListVideo>? = null
}