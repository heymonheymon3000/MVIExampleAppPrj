package com.example.mviexampleappprj.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.request.RequestOptions
import com.example.mviexampleappprj.BaseApplication
import com.example.mviexampleappprj.R
import com.example.mviexampleappprj.ui.BaseActivity
import com.example.mviexampleappprj.util.StateMessageCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Named

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity :
        BaseActivity() {

    @Inject
    @Named("MainFragmentFactory")
    lateinit var mainFragment: FragmentFactory

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    @Inject
    lateinit var providerRequestOptions: RequestOptions

    val viewModel: MainViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeObservers()
        showMainFragment()
    }

    private fun subscribeObservers() {
        viewModel.numActiveJobs.observe(this, {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(this, { stateMessage ->
            stateMessage?.let {
                onResponseReceived(
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

    override fun displayProgressBar(isLoading: Boolean) {
        if(isLoading){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun inject() {
        (application as BaseApplication).mainComponent()
                .inject(this)
    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,
                   MainFragment(providerFactory, providerRequestOptions), "MainFragment")
            .commit()
    }
}