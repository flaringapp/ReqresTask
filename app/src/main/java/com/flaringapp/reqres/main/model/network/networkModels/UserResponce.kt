package com.flaringapp.reqres.main.model.network.networkModels

import com.google.gson.annotations.SerializedName

class UserResponce (
    @SerializedName("data")
    var user: UserWebModel
)