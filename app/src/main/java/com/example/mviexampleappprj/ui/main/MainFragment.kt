package com.example.mviexampleappprj.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mviexampleappprj.R
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.ui.DataStateListener
import com.example.mviexampleappprj.ui.main.state.MainStateEvent.*
import com.example.mviexampleappprj.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ClassCastException

class MainFragment : Fragment(), BlogListAdapter.Interaction {

    override fun onItemSelected(position: Int, item: BlogPost) {
        Timber.d("Clicked $position")
        Timber.d("Clicked $item")
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var dataStateHandler: DataStateListener
    private lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid activity")

        subscribeObservers()
        initRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            Timber.d("$context must implement DataStateListener")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_init -> triggerInitEvent()
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
            R.id.action_clear -> triggerClearEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    @InternalCoroutinesApi
    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            // Handle Loading and Message
            dataStateHandler.onDataStateChange(dataState)

            // handle Data<T>
            dataState.data?.let{ event ->
                event.peekContent().let{ mainViewState ->

                    println("DEBUG: DataState: $mainViewState")

                    var blogs: List<BlogPost> = ArrayList<BlogPost>()
                    if (mainViewState != null) {
                        mainViewState.blogPosts?.let{
                            // set BlogPosts data
                            viewModel.setBlogListData(it)
                        }
                    }

                    if (mainViewState != null) {
                        mainViewState.user?.let{
                            // set User data
                           viewModel.setUser(it)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.blogPosts?.let {blogPosts ->
                // set BlogPosts to RecyclerView
                println("DEBUG: Setting blog posts to RecyclerView: $blogPosts")
                blogListAdapter.submitList(blogPosts)
            }

            viewState.user?.let{ user ->
                // set User data to widgets
                println("DEBUG: Setting User data: $user")
               setUserProperties(user)
            }
        })
    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username
        view?.let{
            Glide.with(it)
                .load(user.image)
                .into(image)
        }
    }

    private fun triggerInitEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
        viewModel.setStateEvent(GetBlogPostsEvent())
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }

    private fun triggerClearEvent() {
        viewModel.setStateEvent(ClearEvent())
    }
}