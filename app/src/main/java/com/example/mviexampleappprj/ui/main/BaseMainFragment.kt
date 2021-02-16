package com.example.mviexampleappprj.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.mviexampleappprj.ui.UICommunicationListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import androidx.fragment.app.viewModels
import com.example.mviexampleappprj.util.StateMessageCallback

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseMainFragment
constructor(
    @LayoutRes
    private val layoutRes: Int
): Fragment(layoutRes){

    lateinit var uiCommunicationListener: UICommunicationListener

    val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChannel()
        subscribeObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Timber.e("$context must implement UICommunicationListener" )
        }
    }

    private fun subscribeObservers() {
        viewModel.numActiveJobs.observe(viewLifecycleOwner, {
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                        response = it.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                )
            }
        })
    }

    private fun setupChannel() = viewModel.setupChannel()
}