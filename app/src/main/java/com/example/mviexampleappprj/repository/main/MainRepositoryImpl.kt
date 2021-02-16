package com.example.mviexampleappprj.repository.main

import com.example.mviexampleappprj.api.ApiService
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.safeApiCall
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
class MainRepositoryImpl
@Inject
constructor(
    val apiMainService: ApiService
): MainRepository{
    override fun getBlogPosts(
            stateEvent: StateEvent):
            Flow<DataState<MainViewState>> = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            apiMainService.getBlogPosts()
        }

        emit(
            object: ApiResponseHandler<MainViewState, List<BlogPost>>(
                    response = apiResult,
                    stateEvent = stateEvent) {
                override suspend fun handleSuccess(resultObj: List<BlogPost>): DataState<MainViewState> {
                    return DataState.data(
                            data = MainViewState(
                                    blogPosts = resultObj
                            ),
                            stateEvent = stateEvent,
                            response = null
                    )
                }
            }.getResult()
        )
    }


    override fun getUser(
        stateEvent: StateEvent,
        userId: String):
        Flow<DataState<MainViewState>> = flow {

            val apiResult = safeApiCall(Dispatchers.IO) {
                apiMainService.getUser(userId)
            }

            emit(
                object: ApiResponseHandler<MainViewState, User>(
                        response = apiResult,
                        stateEvent = stateEvent) {
                    override suspend fun handleSuccess(resultObj: User): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                    user = resultObj
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }
                }.getResult()
            )
    }
}