package com.flaringapp.reqres.main.model.network.networkModels

import com.google.gson.annotations.SerializedName

class PageWebModel(
    @SerializedName("page")
    var page: Int,
    @SerializedName("per_page")
    var usersInPage: Int,
    @SerializedName("total")
    var totalUsers: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    @SerializedName("data")
    var pageUsers: List<PageUserWebModel>
)