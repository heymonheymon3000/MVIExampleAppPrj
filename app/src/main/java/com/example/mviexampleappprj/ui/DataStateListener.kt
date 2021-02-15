package com.example.mviexampleappprj.ui

import com.example.mviexampleappprj.util.DataState

interface DataStateListener {
    fun onDataStateChange(dataState: DataState<*>?)
}