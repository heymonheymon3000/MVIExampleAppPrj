package com.example.mviexampleappprj.repository.main

import com.example.mviexampleappprj.ui.main.state.MainViewState
import com.example.mviexampleappprj.util.DataState
import com.example.mviexampleappprj.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getBlogPosts(stateEvent: StateEvent): Flow<DataState<MainViewState>>
    fun getUser(stateEvent: StateEvent, userId: String): Flow<DataState<MainViewState>>
}