package com.example.mviexampleappprj.ui.main

import androidx.lifecycle.*
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.main.MainRepositoryImpl
import com.example.mviexampleappprj.ui.main.state.MainStateEvent
import com.example.mviexampleappprj.ui.main.state.MainStateEvent.*
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.*
import com.example.mviexampleappprj.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel : ViewModel(){

    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataChannelManager: DataChannelManager<MainViewState>
            = object: DataChannelManager<MainViewState>(){

        override fun handleNewData(data: MainViewState) {
            this@MainViewModel.handleNewData(data)
        }
    }

    fun handleNewData(data: MainViewState) {
        data.user?.let { user ->
            setUser(user)
        }

        data.blogPosts?.let { blogPosts ->
            setBlogListData(blogPosts)
        }
    }

    fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<MainViewState>> = when (stateEvent) {
            is GetBlogPostsEvent -> {
                MainRepositoryImpl.getBlogPosts(stateEvent)
            }

            is GetUserEvent -> {
                MainRepositoryImpl.getUser(stateEvent, stateEvent.userId)
            }

            is ClearEvent -> {
                flow {
                    emit(
                        DataState.data(
                                data = MainViewState(
                                        user = User(),
                                        blogPosts = ArrayList<BlogPost>()
                                ),
                                stateEvent = stateEvent,
                                response = null
                            )
                    )
                }
            }

            else -> {
                flow {
                    emit(
                            DataState.error<MainViewState>(
                                    response = Response(
                                            message = INVALID_STATE_EVENT,
                                            uiComponentType = UIComponentType.None(),
                                            messageType = MessageType.Error()
                                    ),
                                    stateEvent = stateEvent
                            )
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    val numActiveJobs: LiveData<Int>
            = dataChannelManager.numActiveJobs

    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    // FOR DEBUGGING
    fun getMessageStackSize(): Int{
        return dataChannelManager.messageStack.size
    }

    fun setBlogListData(blogPosts: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User){
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return dataChannelManager.isJobAlreadyActive(stateEvent)
    }

    fun setupChannel() = dataChannelManager.setupChannel()


    private fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value?.let {
            it
        }?: MainViewState()
    }

    fun launchJob(
            stateEvent: StateEvent,
            jobFunction: Flow<DataState<MainViewState>>
    ){
        dataChannelManager.launchJob(stateEvent, jobFunction)
    }

    fun setViewState(mainViewState: MainViewState){
        _viewState.value = mainViewState
    }

    fun clearStateMessage(index: Int = 0){
        dataChannelManager.clearStateMessage(index)
    }

    fun areAnyJobsActive(): Boolean{
        return dataChannelManager.numActiveJobs.value?.let {
            it > 0
        }?: false
    }

    private fun cancelActiveJobs(){
        if(areAnyJobsActive()){
            dataChannelManager.cancelJobs()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}

