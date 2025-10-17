package com.example.firstexam

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.firstexam.database.FavoriteEvent
import com.example.firstexam.repository.FavoriteRepository

class DetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun insert(favoriteEvent: FavoriteEvent) {
        mFavoriteRepository.insert(favoriteEvent)
    }

    fun isFavorite(id: Int): LiveData<Boolean> = mFavoriteRepository.isFavorite(id)

    fun delete(favoriteEvent: FavoriteEvent) {
        mFavoriteRepository.delete(favoriteEvent)
    }
    fun deleteById(int: Int){
        mFavoriteRepository.deleteById(int)

    }


}