package com.scandiweb.straume_viewer.Api

import android.content.Context
import com.scandiweb.straume_viewer.Model.PlayListVideo
import com.scandiweb.straume_viewer.Model.PlayListVideoPage
import com.scandiweb.straume_viewer.Model.VideoPage
import io.reactivex.Observable

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import com.scandiweb.straume_viewer.Model.VideoDetails
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit


interface StraumeService {
    @GET("lv/video/jaunakie")
    fun getVideoCategories(): Single<VideoPage>

    @GET
    fun getVideos(@Url url: String): Single<VideoPage>


    @GET
    fun getVideosFromPlaylist(@Url url: String): Single<PlayListVideoPage>

    @GET
    fun getVideoDetails(@Url toVisit: String): Single<VideoDetails>

    companion object Factory {
        fun create(context: Context): StraumeService {
            val responseCache = Cache(File(context.cacheDir, "responses"), 100L * 1024 * 1024)

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    // Add logging interceptor to debug HTTP traffic
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor({ chain ->
                        val originalResponse = chain.proceed(chain.request())
                        val maxAge = 60 * 60 // read from cache for 1 hour
                        originalResponse.newBuilder().header("Cache-Control", "public, max-age=" + maxAge)
                                .build()
                    })
                    .cache(responseCache)
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://straume.lmt.lv/")
                    .addConverterFactory(JspoonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(StraumeService::class.java)
        }
    }
}