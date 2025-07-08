package com.example.fittrack.data.utils

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson


class Converters {
    @TypeConverter
    fun fromLatLngList(json: String): List<LatLng> {
        return Gson().fromJson(json, Array<LatLng>::class.java).toList()
    }
    @TypeConverter
    fun latLngListToJson(list: List<LatLng>): String {
        return Gson().toJson(list)
    }
}
