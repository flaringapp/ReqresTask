package com.flaringapp.reqres.main.model.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.flaringapp.reqres.main.model.objects.ListUser
import io.reactivex.Single

@Dao
interface ListUsersDAO {
    @Query("SELECT * from listUsers")
    fun getAll(): Single<List<ListUser>>

    @Insert(onConflict = REPLACE)
    fun insert(user: ListUser)

    @Insert(onConflict = REPLACE)
    fun insert(user: List<ListUser>)

    @Delete
    fun delete(user: ListUser)

    @Update
    fun update(user: ListUser)

    @Query("DELETE from users")
    fun deleteAll()

    @Query("SELECT * from listUsers WHERE id = :id")
    fun getUserById(id: Int): Single<ListUser>
}
