package com.example.mviexapplication.View

import com.example.mviexapplication.View.Data.MainView
import com.example.mviexapplication.View.Data.ParticalMainState
import com.example.mviexapplication.View.Utils.DataSource
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainPresenter(internal var dataSource: DataSource) :
    MviBasePresenter<MainView, MainViewState>() {

    override fun bindIntents() {

        val gotData = intent ( ViewIntentBinder<MainView,Int> { it.imageIntent } )
            .switchMap {
                index -> dataSource.getImageLinkFromList(index)
                .map{ imageLink -> ParticalMainState.GetImageLink(imageLink) as ParticalMainState }
                .startWith(ParticalMainState.Loadind())
                .onErrorReturn { error -> ParticalMainState.Error(error) }
                .subscribeOn(Schedulers.io())
            }
        val initVieWState = MainViewState(false,false,"", null)
        val initIntent = gotData.observeOn(AndroidSchedulers.mainThread())
            subscribeViewState(initIntent.scan(
                initVieWState,
                BiFunction<MainViewState,ParticalMainState,MainViewState> {
                    prevState,changeState -> this.viewStateReducer(prevState,changeState)
                }
            ))
            {obj,viewState -> obj.render(viewState)}
    }

    internal fun viewStateReducer(prevState: MainViewState,changeState:ParticalMainState):MainViewState{

        if (changeState is ParticalMainState.Loadind){
            prevState.isLoading = true
            prevState.isImageViewShow = false
        }
        if (changeState is ParticalMainState.GetImageLink){
            prevState.isLoading = false
            prevState.isImageViewShow = true
            prevState.imageLink = changeState.imageLink
        }
        if (changeState is ParticalMainState.Error){
            prevState.isLoading = false
            prevState.isImageViewShow = false
            prevState.error = changeState.error
        }
        return prevState
    }
}