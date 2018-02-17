/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.scandiweb.straume_viewer

import android.os.Bundle
import android.support.v17.leanback.app.VideoSupportFragment
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost
import android.support.v17.leanback.media.MediaPlayerGlue
import android.support.v17.leanback.media.PlaybackGlue
import com.scandiweb.straume_viewer.Api.StraumeService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: StraumeViewerMediaPlayerGlue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (_, title, description, _, _, videoUrl) =
                activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as VideoItem




        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        mTransportControlGlue = StraumeViewerMediaPlayerGlue(activity)
        mTransportControlGlue.setMode(MediaPlayerGlue.NO_REPEAT)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
                glue?.let {
                    if (it.isPrepared) {
                        it.play()
                    }
                }
            }
        })

//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
//                .addInterceptor(httpLoggingInterceptor)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)

        val okHttpClient = clientBuilder.build()

        val retrofit = Retrofit.Builder()
                //.baseUrl("https://www.thedroidsonroids.com/")
                .baseUrl("http://straume.lmt.lv/")
                .addConverterFactory(JspoonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        val straumeService: StraumeService = retrofit.create(StraumeService::class.java)

        straumeService
                .getVideoDetails(videoUrl!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ video ->
                    mTransportControlGlue.setTitle(video.title)
                    mTransportControlGlue.setVideoUrl(video.videoURL)
                })
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }
}