package com.example.weatherapp2.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

@Database(entities = [CityCoordinate::class, CityWeatherFullInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun localDao(): LocalDao

    companion object {
        private var INSTANCE: DataBase? = null

        fun getDataBase(context: Context): DataBase? {
            if (INSTANCE == null) {
                synchronized(DataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataBase::class.java,
                        "myDB"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
