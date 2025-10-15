package com.example.firstexam.retrofit

import com.example.firstexam.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvent(
    ): Call<EventResponse>
}
