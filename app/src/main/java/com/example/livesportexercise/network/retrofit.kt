package com.example.livesportexercise.network

import android.util.Log
import com.example.livesportexercise.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
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

    @Provides
    fun provideRetrofit(): LivesportService = LivesportRetrofit
        .create(LivesportService::class.java)

    @Provides
    fun provideCoroutineDispatcher() = CoroutineDispatcherProvider()
}
