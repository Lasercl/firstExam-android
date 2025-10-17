package com.example.firstexam.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite_event")
@Parcelize
class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var name: String = "",
    var mediaCover: String? = null,
) : Parcelable