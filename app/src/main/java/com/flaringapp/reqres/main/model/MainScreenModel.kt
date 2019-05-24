package com.flaringapp.reqres.main.model

import android.content.Context
import com.flaringapp.reqres.main.model.data.User
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.PageWebModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MainScreenModel(
    private val context: Context
) {
    fun requestLoadUsersData(): Single<PageWebModel> {
//        val publishSubject = PublishSubject.create<PageWebModel>()
//
//        val users: ArrayList<User> = ArrayList()
//
//        val publisher = Publi
//
//        return Single.fromCallable {
//            publishSubject
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(
//                    {},
//                    {},
//                    {}
//                )
//        }.subscribeOn(Schedulers.io())
//            .observeOn()

        return NetworkService.instance.getJSONApi().getPage(0)
    }

    private fun loadPage(index: Int): Single<PageWebModel> {
        return NetworkService.instance.getJSONApi().getPage(index)
    }
}