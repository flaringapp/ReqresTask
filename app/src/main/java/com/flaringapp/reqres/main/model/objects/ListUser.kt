package com.flaringapp.reqres.main.model.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listUsers")
data class ListUser(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "avatar_link")
    val avatarLink: String
)