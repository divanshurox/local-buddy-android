package com.example.api

import com.example.api.services.LocalBuddyAPI
import com.example.api.services.LocalBuddyAuthAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object LocalBuddyClient {
    var authToken: String? = null
    private const val baseUrl = "http://localhost:3001/"

    private val authInterceptor = Interceptor { chain ->
        var req = chain.request()
        authToken?.let{
            req = req.newBuilder().header("Authorization",it).build()
        }
        chain.proceed(req)
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okhttpBuilder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)

    val publicApi: LocalBuddyAPI by lazy{
        retrofitBuilder
            .client(okhttpBuilder.build())
            .build()
            .create(LocalBuddyAPI::class.java)
    }

    val authApi: LocalBuddyAuthAPI by lazy{
        retrofitBuilder
            .client(okhttpBuilder.addInterceptor(authInterceptor).build())
            .build()
            .create(LocalBuddyAuthAPI::class.java)
    }

}