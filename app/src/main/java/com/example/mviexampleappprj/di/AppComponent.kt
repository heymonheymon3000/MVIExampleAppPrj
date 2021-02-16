package com.example.mviexampleappprj.di

import android.app.Application
import com.example.mviexampleappprj.di.main.MainComponent
import com.example.mviexampleappprj.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent  {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)


    fun mainComponent(): MainComponent.Factory

}











