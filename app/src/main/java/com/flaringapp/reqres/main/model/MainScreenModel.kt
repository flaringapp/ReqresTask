package com.flaringapp.reqres.main.model

import android.content.Context
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.PageWebModel
import com.flaringapp.reqres.main.model.objects.ListUser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MainScreenModel(
    private val context: Context
) {
    fun requestLoadUsersData(): Observable<List<ListUser>> {

        val resultPublusher = PublishSubject.create<List<ListUser>>()



        return resultPublusher
    }

    private fun loadPage(index: Int, loadListener: PublishSubject<PageWebModel>) {
        val pageLoadDisposable = NetworkService.instance.getJSONApi().getPage(index)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    loadListener.onNext(it)
                },
                {
                    loadListener.onError(it)
                }
            )
    }
}