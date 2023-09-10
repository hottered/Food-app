package com.example.dependencyinjection.db


import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {
    @TypeConverter
    fun fromAnyToString(anything: Any?) : String{
        if(anything == null){
            return ""
        }
        return anything.toString()
    }
    @TypeConverter
    fun fromStringToAny(attribute:String?) : Any {
        if(attribute == null){
            return ""
        }
        return attribute
    }
}