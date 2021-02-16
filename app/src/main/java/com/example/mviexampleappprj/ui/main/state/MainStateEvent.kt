package com.example.mviexampleappprj.ui.main.state

import com.example.mviexampleappprj.util.StateEvent

sealed class MainStateEvent : StateEvent {
    class None : MainStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun toString(): String {
            return "None"
        }
    }

    class GetUserEvent(
        val userId: String
    ): MainStateEvent() {
        override fun errorInfo(): String {
            return "GetUserEvent"
        }

        override fun toString(): String {
            return "GetUserEvent"
        }
    }

    class GetBlogPostsEvent : MainStateEvent() {
        override fun errorInfo(): String {
            return "InitEvent"
        }

        override fun toString(): String {
            return "GetBlogPostsEvent"
        }
    }

    class ClearEvent: MainStateEvent() {
        override fun errorInfo(): String {
            return "ClearEvent"
        }

        override fun toString(): String {
            return "ClearEvent"
        }
    }

    class InitEvent: MainStateEvent() {
        override fun errorInfo(): String {
            return "InitEvent"
        }

        override fun toString(): String {
            return "InitEvent"
        }
    }
}