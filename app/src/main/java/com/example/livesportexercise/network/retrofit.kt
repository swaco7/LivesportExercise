package com.example.livesportexercise.network

import android.util.Log
import com.example.livesportexercise.Config
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val client = OkHttpClient
    .Builder()
    .addInterceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .build()
        Log.e("request", request.toString())
        chain.proceed(request)
    }
    .build()

internal val LivesportRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl(Config.baseUrl)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

internal val liveSportService = LivesportRetrofit
    .create(LivesportService::class.java)
