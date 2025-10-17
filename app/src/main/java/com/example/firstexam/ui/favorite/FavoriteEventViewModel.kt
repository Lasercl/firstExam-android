package com.example.firstexam.ui.favorite

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstexam.database.FavoriteEvent
import com.example.firstexam.repository.FavoriteRepository
import com.example.firstexam.response.EventResponse
import com.example.firstexam.response.ListEventsItem
import com.example.firstexam.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteEventViewModel(application: Application) : AndroidViewModel(application) {

    private val mFavoriteRepository = FavoriteRepository(application)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listItem = MutableLiveData<List<ListEventsItem>?>()
    val listItem: LiveData<List<ListEventsItem>?> = _listItem

    // Simpan hasil favorit dari database
    private val favorites = mFavoriteRepository.getAllFavorites()

    init {
        getEvent()
    }

    private fun getEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()?.listEvents as List<ListEventsItem>?

                    if (responseBody != null) {
                        // observe favorit lalu filter
                        favorites.observeForever { favs ->
                            val filtered = filterFinishedEvents(responseBody, favs)
                            _listItem.value = filtered
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun filterFinishedEvents(
        responseBody: List<ListEventsItem>?,
        favoriteEvent: List<FavoriteEvent>?
    ): List<ListEventsItem>? {
        val favoriteIds = favoriteEvent?.map { it.id }?.toSet() ?: emptySet()
        return responseBody?.filter { event ->
            event.id in favoriteIds
        } ?: emptyList()
    }

    fun getFavorite(): LiveData<List<FavoriteEvent>> = favorites

    fun update(favoriteEvent: FavoriteEvent) {
        mFavoriteRepository.update(favoriteEvent)
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        mFavoriteRepository.delete(favoriteEvent)
    }
}
