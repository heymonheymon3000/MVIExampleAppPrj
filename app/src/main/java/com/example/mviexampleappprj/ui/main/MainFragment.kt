package com.example.mviexampleappprj.ui.main

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mviexampleappprj.R
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.ui.main.state.MainStateEvent.*
import com.example.mviexampleappprj.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class MainFragment :
    BaseMainFragment(R.layout.fragment_main),
    BlogListAdapter.Interaction {

    private lateinit var blogListAdapter: BlogListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initRecyclerView()
        subscribeObservers()
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

    override fun onItemSelected(position: Int, item: BlogPost) {
        Timber.d("Clicked $position")
        Timber.d("Clicked $item")
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
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

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
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