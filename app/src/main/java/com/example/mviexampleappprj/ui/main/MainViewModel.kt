package com.example.mviexampleappprj.ui.main

import androidx.lifecycle.*
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.repository.main.MainRepository
import com.example.mviexampleappprj.ui.main.state.MainStateEvent
import com.example.mviexampleappprj.ui.main.state.MainStateEvent.*
import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.AbsentLiveData
import com.example.mviexampleappprj.util.DataState
import kotlinx.coroutines.InternalCoroutinesApi

class MainViewModel : ViewModel(){

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState

    @InternalCoroutinesApi
    val dataState: LiveData<DataState<MainViewState>> = Transformations
            .switchMap(_stateEvent){stateEvent ->
                stateEvent?.let {
                    handleStateEvent(stateEvent)
                }
            }



    @InternalCoroutinesApi
    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>>{
        println("DEBUG: New StateEvent detected: $stateEvent")
        return when(stateEvent){
//            is InitEvent -> {
//                return object: LiveData<DataState<MainViewState>>(){
//                    override fun onActive() {
//                        super.onActive()
//
//                        val blogPosts: ArrayList<BlogPost> = ArrayList()
//                        blogPosts.add(
//                                BlogPost(
//                                        pk = 0,
//                                        title = "Vancouver PNE 2019",
//                                        body = "Here is Jess and I at the Vancouver PNE. We ate a lot of food.",
//                                        image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image8.jpg"
//                                )
//                        )
//                        blogPosts.add(
//                                BlogPost(
//                                        pk = 1,
//                                        title = "Ready for a Walk",
//                                        body = "Here I am at the park with my dogs Kiba and Maizy. Maizy is the smaller one and Kiba is the larger one.",
//                                        image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image2.jpg"
//                                )
//                        )
//
//                        val user = User(
//                                email = "mitch@tabian.ca",
//                                username = "mitch",
//                                image = "https://cdn.open-api.xyz/open-api-static/static-random-images/logo_1080_1080.png"
//                        )
//
//                        value = DataState.data(
//                                data = MainViewState(user =  user, blogPosts = blogPosts)
//                        )
//
//                    }
//                }
//            }

            is GetBlogPostsEvent -> {
                MainRepository.getBlogPosts()
            }

            is GetUserEvent -> {
                MainRepository.getUser(stateEvent.userId)
            }

            is ClearEvent -> {
                val result = MediatorLiveData<DataState<MainViewState>>()
                result.value = DataState.data(
                        data = MainViewState(user =  User(null, null, null), blogPosts = ArrayList())
                )
                return result
            }

            is None -> {
                AbsentLiveData.create()
            }
            else -> {
                AbsentLiveData.create()
            }
        }
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

    private fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value?.let {
            it
        }?: MainViewState()
    }

    fun setStateEvent(event: MainStateEvent){
        val state: MainStateEvent = event
        _stateEvent.value = state
    }
}

//class MainViewModel: ViewModel() {
//
//    // the state event triggered
//    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
//
//    // data that is being returned to the view
//    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()
//
//
//    val viewState: LiveData<MainViewState>
//        get() = _viewState
//
//    val dataState: LiveData<DataState<MainViewState>> = Transformations
//        .switchMap(_stateEvent) { stateEvent ->
//            stateEvent?.let {
//                handleStateEvent(it)
//            }
//        }
//
//
//    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
//        return when (stateEvent) {
//            is None -> {
//                AbsentLiveData.create()
//            }
//
//            is GetUserEvent -> {
//                MainRepository.getUser(stateEvent.userId)
//            }
//
//            is GetBlogPostsEvent -> {
//                MainRepository.getBlogPosts()
//            }
//
//            is ClearEvent -> {
//                val result = MediatorLiveData<DataState<MainViewState>>()
//                result.value = DataState.data(
//                    data = MainViewState(user =  User(null, null, null), blogPosts = ArrayList())
//                )
//                return result
//            }
//        }
//    }
//
//    fun setBlogListData(blogPosts: List<BlogPost>) {
//        val update = getCurrentViewStateOrNew()
//        update.blogPosts = blogPosts
//        _viewState.value = update
//    }
//
//    fun setUserData(user: User) {
//        val update = getCurrentViewStateOrNew()
//        update.user = user
//        _viewState.value = update
//    }
//
//    fun setInitData(user: User, blogPosts: List<BlogPost>) {
//        val update = getCurrentViewStateOrNew()
//        update.user = user
//        update.blogPosts = blogPosts
//        _viewState.value = update
//    }
//
//    private fun getCurrentViewStateOrNew(): MainViewState {
//        return viewState.value ?: MainViewState()
//    }
//
//    fun setStateEvent(event: MainStateEvent) {
//        _stateEvent.value = event
//    }
//}

//                return object: LiveData<DataState<MainViewState>>(){
//                    override fun onActive() {
//                        super.onActive()
//                        val blogList: ArrayList<BlogPost> = ArrayList()
//                        blogList.add(
//                            BlogPost(
//                                pk = 0,
//                                title = "Vancouver PNE 2019",
//                                body = "Here is Jess and I at the Vancouver PNE. We ate a lot of food.",
//                                image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image8.jpg"
//                            )
//                        )
//                        blogList.add(
//                            BlogPost(
//                                pk = 1,
//                                title = "Ready for a Walk",
//                                body = "Here I am at the park with my dogs Kiba and Maizy. Maizy is the smaller one and Kiba is the larger one.",
//                                image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image2.jpg"
//                            )
//                        )
//
//                        val user = User(
//                            email = "mitch@tabian.ca",
//                            username = "mitch",
//                            image = "https://cdn.open-api.xyz/open-api-static/static-random-images/logo_1080_1080.png"
//                        )
//
//                        value = DataState.data(message = null,
//                            data = MainViewState(blogPosts = blogList, user = user)
//                        )
//                    }
//                }