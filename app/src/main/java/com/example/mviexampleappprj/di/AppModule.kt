package com.example.mviexampleappprj.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mviexampleappprj.R
import com.example.mviexampleappprj.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule{
    @JvmStatic
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(gsonBuilder:  Gson): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

}