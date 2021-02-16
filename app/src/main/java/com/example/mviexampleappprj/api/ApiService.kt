package com.example.mviexampleappprj.api

import com.example.mviexampleappprj.di.main.MainScope
import com.example.mviexampleappprj.model.BlogPost
import com.example.mviexampleappprj.model.User
import retrofit2.http.GET
import retrofit2.http.Path

@MainScope
interface ApiService {
    @GET("placeholder/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : User

    @GET("placeholder/blogs")
    suspend fun getBlogPosts() : List<BlogPost>
}