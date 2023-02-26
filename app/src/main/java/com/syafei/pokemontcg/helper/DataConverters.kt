package com.syafei.pokemontcg.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.syafei.pokemontcg.coredata.model.ImageData
import java.lang.reflect.Type

class DataConverters {

    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToImages(value: String?): ImageData? {
        val listType: Type = object : TypeToken<ImageData?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromImages(images: ImageData?): String? {
        val gson = Gson()
        return gson.toJson(images)
    }


}