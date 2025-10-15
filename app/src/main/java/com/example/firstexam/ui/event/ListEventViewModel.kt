package com.example.firstexam.ui.event

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstexam.response.EventResponse
import com.example.firstexam.response.ListEventsItem
import com.example.firstexam.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ListEventViewModel : ViewModel() {

    private val _listItem = MutableLiveData<List<ListEventsItem>?>()
    val listItem: MutableLiveData<List<ListEventsItem>?> = _listItem
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init {
        getEvent()
    }

    private fun getEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    var responseBody:List<ListEventsItem>?=response.body()?.listEvents as List<ListEventsItem>?
                    var upcomingEvent= filterUpcomingEvents(responseBody)
                    _listItem.value=upcomingEvent
//                    _listReview.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    private fun filterUpcomingEvents(responseBody: List<ListEventsItem>?): List<ListEventsItem> ?{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val now = Date()

        return responseBody?.filter { event ->
            try {
                val eventDate = sdf.parse(event.beginTime)
                eventDate != null && eventDate.after(now)
            } catch (e: Exception) {
                false
            }
        }
    }
}