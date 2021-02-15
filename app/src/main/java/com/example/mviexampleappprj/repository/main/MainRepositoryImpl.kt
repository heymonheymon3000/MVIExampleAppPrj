package com.example.mviexampleappprj.repository.main

import com.example.mviexampleappprj.api.RetrofitBuilder
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.safeApiCall
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@FlowPreview
object MainRepositoryImpl : MainRepository{
    override fun getBlogPosts(
            stateEvent: StateEvent):
            Flow<DataState<MainViewState>> = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            RetrofitBuilder.apiService.getBlogPosts()
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
                RetrofitBuilder.apiService.getUser(userId)
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



//private val returnedData = MediatorLiveData<DataState<MainViewState>>()
//
//@InternalCoroutinesApi
//fun getBlogPosts(): Flow<DataState<MainViewState>> {
//    return object: NetworkBoundResource<List<BlogPost>, MainViewState>() {
//        override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
//            CoroutineScope(Main).launch {
//                result.value = DataState.data(
//                        data = MainViewState(blogPosts =  response.body))
//            }
//        }
//
//        override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
//            return RetrofitBuilder.apiService.getBlogPosts()
//        }
//    }.asLiveData()
//}
//
//@InternalCoroutinesApi
//fun getUser(userId: String): LiveData<DataState<MainViewState>> {
//    return object: NetworkBoundResource<User, MainViewState>() {
//        override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
//            CoroutineScope(Main).launch {
//                result.value = DataState.data(
//                        data = MainViewState(user =  response.body)
//                )
//            }
//
//        }
//
//        override fun createCall(): LiveData<GenericApiResponse<User>> {
//            return RetrofitBuilder.apiService.getUser(userId)
//        }
//    }.asLiveData()
//}