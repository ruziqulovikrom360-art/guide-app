package com.example.guidemetestapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.guidemetestapp.data.api.AuthInterceptor
import com.example.guidemetestapp.data.api.ErrorInterceptor
import com.example.guidemetestapp.data.api.TokenAuthenticator
import com.example.guidemetestapp.data.api.GuideApi
import com.example.guidemetestapp.data.api.TourApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = 
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .maxContentLength(250000L)
            .alwaysReadResponseBody(true)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(ErrorInterceptor(context))
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://guide-me-8znn.onrender.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideGuideApi(retrofit: Retrofit): GuideApi = retrofit.create(GuideApi::class.java)

    @Provides
    @Singleton
    fun provideTourApiService(retrofit: Retrofit): TourApiService = retrofit.create(TourApiService::class.java)
}
