package kz.iitu.newsapp.db

import androidx.room.TypeConverter
import kz.iitu.newsapp.models.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }

}