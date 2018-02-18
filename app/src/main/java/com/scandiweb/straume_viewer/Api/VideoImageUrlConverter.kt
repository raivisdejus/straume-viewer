package com.scandiweb.straume_viewer.Api

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector

class VideoImageUrlConverter : ElementConverter<String> {
    override fun convert(node: Element?, selector: Selector?): String {
        return node
            ?.select(selector?.value)
            ?.attr(selector?.attr)
            ?.split(" ")
            ?.component3() ?: ""
    }
}
