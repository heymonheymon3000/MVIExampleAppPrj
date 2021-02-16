package com.example.mviexampleappprj.di.main

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mviexampleappprj.fragments.MainFragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object MainFragmentsModule {

    @JvmStatic
    @MainScope
    @Provides
    @Named("MainFragmentFactory")
    fun provideMainFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestOptions: RequestOptions,
        requestManager: RequestManager
    ): FragmentFactory {
        return MainFragmentFactory(
            viewModelFactory,
            requestOptions,
            requestManager
        )
    }
}