package com.example.mviexampleappprj.fragments

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mviexampleappprj.di.main.MainScope
import com.example.mviexampleappprj.ui.main.MainFragment
import javax.inject.Inject

@MainScope
class MainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions,
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            MainFragment::class.java.name -> {
                MainFragment(viewModelFactory)
            }

            else -> {
                MainFragment(viewModelFactory)
            }
        }


}