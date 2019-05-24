package com.flaringapp.reqres.main.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList


internal class TypeConverter {

    companion object {

        private val gson = Gson()

        @TypeConverter @JvmStatic
        fun stringToIntList(data: String?): ArrayList<Int>? {
            if (data == null) {
                return ArrayList()
            }

            val listType = object : TypeToken<ArrayList<Int>>() {}.type

            return gson.fromJson(data, listType)
        }

        @TypeConverter @JvmStatic
        fun intListToString(someObjects: ArrayList<Int>): String? {
            return gson.toJson(someObjects)
        }
    }
}