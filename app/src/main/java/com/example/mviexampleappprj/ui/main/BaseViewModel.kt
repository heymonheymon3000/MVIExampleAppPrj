package com.example.mviexampleappprj.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mviexampleappprj.util.DataChannelManager
import com.example.mviexampleappprj.util.DataState
import com.example.mviexampleappprj.util.StateEvent
import com.example.mviexampleappprj.util.StateMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState> : ViewModel() {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val viewState: LiveData<ViewState>
        get() = _viewState

    private val dataChannelManager: DataChannelManager<ViewState>
            = object: DataChannelManager<ViewState>(){

        override fun handleNewData(data: ViewState) {
            this@BaseViewModel.handleNewData(data)
        }
    }

    val numActiveJobs: LiveData<Int> = dataChannelManager.numActiveJobs
    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage


    // FOR DEBUGGING
    fun getMessageStackSize(): Int{
        return dataChannelManager.messageStack.size
    }

    fun areAnyJobsActive(): Boolean{
        return dataChannelManager.numActiveJobs.value?.let {
            it > 0
        }?: false
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>>) {

        dataChannelManager.launchJob(stateEvent, jobFunction)
    }

    fun setViewState(mainViewState: ViewState){
        _viewState.value = mainViewState
    }

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value ?: initNewViewState()
    }

    fun setupChannel() = dataChannelManager.setupChannel()

    fun clearStateMessage(index: Int = 0){
        dataChannelManager.clearStateMessage(index)
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return dataChannelManager.isJobAlreadyActive(stateEvent)
    }

    fun cancelActiveJobs(){
        if(areAnyJobsActive()){
            dataChannelManager.cancelJobs()
        }
    }
    abstract fun handleNewData(data: ViewState)

    abstract fun initNewViewState(): ViewState
}