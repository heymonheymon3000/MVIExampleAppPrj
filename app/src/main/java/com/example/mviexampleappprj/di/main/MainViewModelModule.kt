package com.example.mviexampleappprj.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mviexampleappprj.di.main.keys.MainViewModelKey
import com.example.mviexampleappprj.ui.main.MainViewModel
import com.example.mviexampleappprj.viewModels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

}