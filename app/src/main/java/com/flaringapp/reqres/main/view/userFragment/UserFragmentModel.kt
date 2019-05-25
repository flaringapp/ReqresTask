package com.flaringapp.reqres.main.view.userFragment

import android.content.Context
import com.flaringapp.reqres.main.model.database.UsersDatabase
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.UserWebModel
import com.flaringapp.reqres.main.model.objects.User
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class UserFragmentModel(
    private var context: Context
) {

    private val disposables: ArrayList<Disposable> = ArrayList()

    fun destroy() {
        for (disposable in disposables) disposable.dispose()
        disposables.clear()
    }

    fun loadUser(id: Int): Single<User> {
        return UsersDatabase.getInstance(context)!!.usersDAO().getUserById(id)
    }

    fun updateUser(id: Int):Single<User> {
        val resultPublisher = PublishSubject.create<User>()

        disposables += NetworkService.instance.getJSONApi().getUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    val user = webUserToUser(it)
                    saveUser(user)
                    resultPublisher.onNext(user)
                },
                {
                    resultPublisher.onError(it)
                }
            )

        return resultPublisher.singleOrError()
    }

    private fun saveUser(user: User) {
        UsersDatabase.getInstance(context)!!.usersDAO().insert(user)
    }

    private fun webUserToUser(webUser: UserWebModel): User {
        return User(
            webUser.id,
            webUser.email,
            webUser.firstName,
            webUser.lastName,
            webUser.avatarLink
        )
    }
}