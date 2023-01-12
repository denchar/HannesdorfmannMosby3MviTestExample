package com.example.mviexapplication.View.Data

import com.example.mviexapplication.View.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView:MvpView {

    val imageIntent:Observable<Int>

    fun render(viewState: MainViewState)
}