package com.flaringapp.reqres.main.model.network.networkModels

import com.google.gson.annotations.SerializedName

class PageUserWebModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("email")
    var email: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("avatar")
    var avatarLink: String
)