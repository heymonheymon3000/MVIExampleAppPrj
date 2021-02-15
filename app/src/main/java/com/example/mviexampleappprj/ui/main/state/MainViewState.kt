package com.example.mviexampleappprj.ui.main.state

import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import com.example.mviexampleappprj.util.StateEvent

data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
)