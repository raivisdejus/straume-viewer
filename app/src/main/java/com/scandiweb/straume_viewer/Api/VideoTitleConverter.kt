package com.scandiweb.straume_viewer.Api

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector

class VideoTitleConverter : ElementConverter<String> {
    override fun convert(node: Element?, selector: Selector?): String {
        // remove unneeded extra tags
        node?.select(".category-title")?.remove()

        return node
                ?.select(selector?.value)
                ?.text() ?: ""
    }
}
