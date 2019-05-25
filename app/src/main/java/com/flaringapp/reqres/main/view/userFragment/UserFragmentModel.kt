package com.flaringapp.reqres.main.view.userFragment

import android.content.Context
import com.flaringapp.reqres.main.model.database.UsersDatabase
import com.flaringapp.reqres.main.model.network.NetworkService
import com.flaringapp.reqres.main.model.network.networkModels.UserResponce
import com.flaringapp.reqres.main.model.objects.User
import io.reactivex.Observable
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

    fun updateUser(id: Int):Observable<User> {
        val resultPublisher = PublishSubject.create<User>()

        disposables += NetworkService.instance.getJSONApi().getUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    val user = webResponseToUser(it)
                    saveUser(user)
                    resultPublisher.onNext(user)
                },
                {
                    resultPublisher.onError(it)
                }
            )

        return resultPublisher
    }

    private fun saveUser(user: User) {
        UsersDatabase.getInstance(context)!!.usersDAO().insert(user)
    }

    private fun webResponseToUser(userResponse: UserResponce): User {
        return User(
            userResponse.user.id,
            userResponse.user.email,
            userResponse.user.firstName,
            userResponse.user.lastName,
            userResponse.user.avatarLink
        )
    }
}