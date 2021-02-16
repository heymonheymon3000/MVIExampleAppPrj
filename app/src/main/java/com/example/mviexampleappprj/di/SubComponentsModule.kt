package com.example.mviexampleappprj.di

import com.example.mviexampleappprj.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        MainComponent::class
    ]
)
class SubComponentsModule