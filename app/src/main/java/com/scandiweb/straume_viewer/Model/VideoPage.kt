package com.scandiweb.straume_viewer.Model

import pl.droidsonroids.jspoon.annotation.Selector

class VideoPage {
    @Selector("#video-scroll-menu li.selected a")
    var title: String? = null
    @Selector("#video-scroll-menu li")
    var menus: List<MenuItem>? = null
    @Selector(".video-item")
    var videos: List<Video>? = null
}