package com.example.mviexampleappprj.ui.main.state

import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User

data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
)