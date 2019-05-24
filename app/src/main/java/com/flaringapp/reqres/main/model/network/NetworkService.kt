package com.flaringapp.reqres.main.model.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class NetworkService private constructor() {
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getJSONApi(): JSONPlaceHolderApi {
        return retrofit.create(JSONPlaceHolderApi::class.java)
    }

    companion object {
        private var mInstance: NetworkService? = null
        private val BASE_URL = "https://reqres.in/api/"

        val instance: NetworkService
            get() {
                if (mInstance == null) {
                    mInstance = NetworkService()
                }
                return mInstance as NetworkService
            }
    }
}