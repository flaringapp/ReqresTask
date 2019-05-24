package com.flaringapp.reqres.main.model

import android.content.Context
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.PageWebModel
import com.flaringapp.reqres.main.model.objects.ListUser
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MainScreenModel(
    private val context: Context
) {
    /**
     * Loads users list from internet using pagination, returns
     * array of users from each loaded page
     * Returns flowable that should be subscribed to listen for
     * users loading
     */
    fun requestLoadUsersData(): Flowable<List<ListUser>> {
        //Data observable
        val resultPublisher = PublishSubject.create<List<ListUser>>()

        //Loader observable (loads and proceeds web data), notifies data observers
        val loadListener = PublishSubject.create<PageWebModel>()
        val loadDisposable = loadListener
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doAfterNext { }
            .subscribe(
                {
                    resultPublisher.onNext(getListUsersFromPage(it))
                    if (it.page == it.totalPages) resultPublisher.onComplete()
                    else loadPage(it.page + 1, loadListener)
                },
                {
                    resultPublisher.onError(it)
                }
            )

        //Load first page
        loadPage(0, loadListener)

        return resultPublisher.toFlowable(BackpressureStrategy.BUFFER)
    }

    /**
     * Converts web user object to data one
     */
    private fun getListUsersFromPage(page: PageWebModel): List<ListUser> {
        return List(page.usersInPage) { i ->
            ListUser(
                page.pageUsers[i].id, page.pageUsers[i].firstName, page.pageUsers[i].lastName,
                page.pageUsers[i].avatarLink
            )
        }
    }

    /**
     * Loads page with index and notifies publisher
     * @param index index of page that should be loaded (>0)
     */
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