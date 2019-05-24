package com.flaringapp.reqres.main.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flaringapp.reqres.main.model.objects.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun usersDataDAO(): UsersDataDAO

    companion object {
        private var INSTANCE: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase? {
            if (INSTANCE == null) {
                synchronized(UsersDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UsersDatabase::class.java, "users.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}