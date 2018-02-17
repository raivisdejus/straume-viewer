package com.scandiweb.straume_viewer.Model

import pl.droidsonroids.jspoon.annotation.Selector

class MenuItem {
    @Selector("a")
    var title: String? = null
    @Selector(value = "a", attr = "href")
    var url: String? = null
}