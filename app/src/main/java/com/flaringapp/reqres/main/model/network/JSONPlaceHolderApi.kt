package com.flaringapp.reqres.main.model.network

import com.flaringapp.reqres.main.model.network.networkModels.PageWebModel
import com.flaringapp.reqres.main.model.network.networkModels.UserResponce
import com.flaringapp.reqres.main.model.network.networkModels.UserWebModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JSONPlaceHolderApi {
    @GET("users")
    fun getPage(@Query("page") pageNumber: Int): Single<PageWebModel>

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Single<UserResponce>
}