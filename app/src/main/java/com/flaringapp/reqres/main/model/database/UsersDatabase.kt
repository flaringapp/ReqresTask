package com.flaringapp.reqres.main.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flaringapp.reqres.main.model.objects.ListUser
import com.flaringapp.reqres.main.model.objects.User

@Database(entities = [User::class, ListUser::class], version = 1, exportSchema = false)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun usersDAO(): UsersDAO
    abstract fun listUsersDAO(): ListUsersDAO

    companion object {
        private var instance: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase? {
            if (instance == null) {
                synchronized(UsersDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        UsersDatabase::class.java, "users.db")
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}