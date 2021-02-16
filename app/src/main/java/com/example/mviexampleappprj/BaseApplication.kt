package com.example.mviexampleappprj

import android.app.Application
import com.example.mviexampleappprj.di.AppComponent
import com.example.mviexampleappprj.di.DaggerAppComponent
import com.example.mviexampleappprj.di.main.MainComponent
import timber.log.Timber

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent


    private var mainComponent: MainComponent? = null


    override fun onCreate() {
        super.onCreate()

        initAppComponent()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    fun releaseMainComponent(){
        mainComponent = null
    }

    fun mainComponent(): MainComponent {
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    private fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}