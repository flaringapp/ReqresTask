package com.flaringapp.reqres.main.model

import android.content.Context
import com.flaringapp.reqres.main.model.database.UsersDatabase
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.PageWebModel
import com.flaringapp.reqres.main.model.objects.ListUser
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MainScreenModel(
    private val context: Context
) {

    private val disposables: ArrayList<Disposable> = ArrayList()

    fun destroy() {
        for(disposable in disposables) disposable.dispose()
    }

    /**
     * Loads users list from internet using pagination, returns
     * array of users from each loaded page
     * Returns flowable that should be subscribed to listen for
     * users loading
     */
    fun downloadUsersData(): Flowable<List<ListUser>> {
        //Data observable
        val resultPublisher = PublishSubject.create<List<ListUser>>()

        val usersList = ArrayList<ListUser>()

        //Loader observable (loads and proceeds web data), notifies data observers
        val loadListener = PublishSubject.create<PageWebModel>()
        disposables += loadListener
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doAfterNext { }
            .subscribe(
                {
                    val users = getListUsersFromPage(it)

                    usersList.addAll(users)
                    addUsersToDatabase(users)

                    if (it.page != it.totalPages){
                        loadPage(it.page + 1, loadListener)
                    } else {
                        resultPublisher.onNext(usersList.toList())
                        resultPublisher.onComplete()

                        deleteUnusedRecords(usersList)
                    }

                },
                {
                    resultPublisher.onError(it)
                }
            )

        //Load first page
        loadPage(1, loadListener)

        return resultPublisher.toFlowable(BackpressureStrategy.BUFFER)
    }

    /**
     * Loads page with index and notifies publisher
     * @param index index of page that should be loaded (>0)
     */
    private fun loadPage(index: Int, loadListener: PublishSubject<PageWebModel>) {
        disposables += NetworkService.instance.getJSONApi().getPage(index)
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

    private fun addUsersToDatabase(users: List<ListUser> ) {
        UsersDatabase.getInstance(context)!!.listUsersDAO().insert(users)
    }

    /**
     * Clears old records from the database
     * Call this function only passing whole list of users
     */
    private fun deleteUnusedRecords(users: ArrayList<ListUser>) {
        disposables += UsersDatabase.getInstance(context)!!.listUsersDAO().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    for (i in 0 until it.size) {
                        if (!users.contains(it[i])) {
                            UsersDatabase.getInstance(context)!!.listUsersDAO().delete(it[i])
                        }
                    }
                }, {}
            )
    }

    fun loadUsers(): Single<List<ListUser>> {
        return UsersDatabase.getInstance(context)!!.listUsersDAO().getAll()
    }
}