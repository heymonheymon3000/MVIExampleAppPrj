package com.example.mviexampleappprj.ui.main.state

sealed class MainStateEvent {
    class None : MainStateEvent()
    class GetUserEvent(
        val userId: String
    ): MainStateEvent()
    class GetBlogPostsEvent : MainStateEvent()
    class ClearEvent: MainStateEvent()
    class InitEvent: MainStateEvent()
}