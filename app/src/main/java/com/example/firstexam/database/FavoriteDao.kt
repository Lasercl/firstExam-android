package com.example.firstexam.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface  FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event: FavoriteEvent)
    @Update
    fun update(event: FavoriteEvent)
    @Delete
    fun delete(event: FavoriteEvent)
    @Query("SELECT * from favorite_event ORDER BY id ASC")
    fun getAllEvent(): LiveData<List<FavoriteEvent>>

    @Query("SELECT COUNT(*) FROM favorite_event WHERE id = :id")
    fun countById(id: Int): LiveData<Int>

    @Query("DELETE FROM favorite_event WHERE id = :id")
    fun deleteById(id: Int)

}