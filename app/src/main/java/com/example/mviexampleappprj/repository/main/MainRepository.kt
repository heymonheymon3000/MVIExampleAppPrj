package com.example.mviexampleappprj.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mviexampleappprj.api.RetrofitBuilder
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.NetworkBoundResource
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.ApiSuccessResponse
import com.example.mviexampleappprj.util.DataState
import com.example.mviexampleappprj.util.GenericApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

object MainRepository {

    private val returnedData = MediatorLiveData<DataState<MainViewState>>()

    @InternalCoroutinesApi
    fun getBlogPosts(): LiveData<DataState<MainViewState>>{
        return object: NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                CoroutineScope(Main).launch {
                result.value = DataState.data(
                    data = MainViewState(blogPosts =  response.body))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPosts()
            }
        }.asLiveData()
    }

    @InternalCoroutinesApi
    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return object: NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                CoroutineScope(Main).launch {
                    result.value = DataState.data(
                            data = MainViewState(user =  response.body)
                    )
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }
        }.asLiveData()
    }

    @InternalCoroutinesApi
    fun init(userId: String): LiveData<DataState<MainViewState>> {

        val source1 = getUser(userId)
        val source2 = getBlogPosts()

        returnedData.addSource(source1) { s ->
            Timber.i(s.data?.peekContent()?.user.toString())
            returnedData.value = DataState.data(
                    data = MainViewState(user =  s.data?.peekContent()?.user)
            )
            returnedData.removeSource(source1)

        }

        returnedData.addSource(source2) { s ->
            Timber.i(s.data?.peekContent()?.blogPosts.toString())
            returnedData.removeSource(source2)
            returnedData.value = DataState.data(
                    data = MainViewState(blogPosts =  s.data?.peekContent()?.blogPosts)
            )
        }

        return returnedData
    }
}