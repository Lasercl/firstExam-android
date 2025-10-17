package com.example.firstexam.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.firstexam.database.FavoriteDao
import com.example.firstexam.database.FavoriteEvent
import com.example.firstexam.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.lifecycle.map

class FavoriteRepository (application: Application){
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }
    fun isFavorite(id: Int): LiveData<Boolean> {
        val result = MediatorLiveData<Boolean>()
        val source = mFavoriteDao.countById(id)
        result.addSource(source) { count ->
            result.value = count > 0
        }
        return result
    }


    fun getAllFavorites(): LiveData<List<FavoriteEvent>> = mFavoriteDao.getAllEvent()
    fun insert(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.insert(favoriteEvent) }
    }
    fun delete(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.delete(favoriteEvent) }
    }
    fun update(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.update(favoriteEvent) }
    }
    fun deleteById(id: Int){
        executorService.execute { mFavoriteDao.deleteById(id) }

    }

}