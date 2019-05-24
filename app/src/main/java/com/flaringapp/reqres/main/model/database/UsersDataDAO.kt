package com.flaringapp.reqres.main.model.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.flaringapp.reqres.main.model.objects.User
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UsersDataDAO {
    @Query("SELECT * from users")
    fun getAll(): Flowable<List<User>>

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("DELETE from users")
    fun deleteAll()

    @Query("SELECT * from users WHERE id = :id")
    fun getUserById(id: Int): Single<User>
}
