package com.scandiweb.straume_viewer.Model

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector


class VideoDetails {
    @Selector(".video-heading h1")
    var title: String? = null

    @Selector("#video-description p")
    var description: String? = null

    @Selector("script")
    var scripts :  ArrayList<Element>? = null

    var videoURL: String ? = null
        get() {
            for (script in this.scripts !!) {
                val scriptContent = script.toString()
                val httpPos = scriptContent.indexOf("http")
                val mp4Pos = scriptContent.indexOf(".mp4")
                if (httpPos != -1 && mp4Pos != -1) {
                    return scriptContent.substring(httpPos, mp4Pos + ".mp4".length)
                }
            }
            return null
        }

    override fun toString(): String {
        return "VideoDetails(title=$title, \n\n " +
                "description=$description) \n\n " +
                "number of script to parse ${scripts?.size} \n\n "+
                "movie url = $videoURL "
    }

}