package com.example.mviexampleappprj.ui.main

import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.main.MainRepository
import com.example.mviexampleappprj.ui.main.state.MainStateEvent.*
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.*
import com.example.mviexampleappprj.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel
@Inject
constructor(
    val mainRepository: MainRepository
) :
    BaseViewModel<MainViewState>() {

    @FlowPreview
    fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MainViewState>> = when (stateEvent) {

            is GetBlogPostsEvent -> {
                mainRepository.getBlogPosts(stateEvent)
            }

            is GetUserEvent -> {
                mainRepository.getUser(stateEvent, stateEvent.userId)
            }

            is ClearEvent -> {
                flow {
                    emit(
                        DataState.data(
                            data = MainViewState(
                                    user = User(),
                                    blogPosts = ArrayList()
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

    override fun handleNewData(data: MainViewState) {
        data.user?.let { user ->
            setUser(user)
        }

        data.blogPosts?.let { blogPosts ->
            setBlogListData(blogPosts)
        }
    }

    private fun setBlogListData(blogPosts: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        setViewState(update)
    }

    private fun setUser(user: User){
        val update = getCurrentViewStateOrNew()
        update.user = user
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }
}

