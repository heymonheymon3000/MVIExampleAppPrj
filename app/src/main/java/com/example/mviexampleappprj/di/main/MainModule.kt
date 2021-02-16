package com.example.mviexampleappprj.di.main


import com.example.mviexampleappprj.api.ApiService
import com.example.mviexampleappprj.repository.main.MainRepository
import com.example.mviexampleappprj.repository.main.MainRepositoryImpl


import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit


@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService {
        return retrofitBuilder
                .build()
                .create(ApiService::class.java)
    }

    @FlowPreview
    @JvmStatic
    @MainScope
    @Provides
    fun provideMainRepository(
        apiService: ApiService
    ): MainRepository {
        return MainRepositoryImpl(apiService)
    }


}