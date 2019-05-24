package com.flaringapp.reqres.main.model.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.flaringapp.reqres.main.model.objects.ListUser
import com.flaringapp.reqres.main.model.objects.User
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ListUsersDataDAO {
    @Query("SELECT * from listUsers")
    fun getAll(): Flowable<List<User>>

    @Insert(onConflict = REPLACE)
    fun insert(user: ListUser)

    @Delete
    fun delete(user: ListUser)

    @Update
    fun update(user: ListUser)

    @Query("DELETE from users")
    fun deleteAll()

    @Query("SELECT * from listUsers WHERE id = :id")
    fun getUserById(id: Int): Single<ListUser>
}
