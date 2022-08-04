package com.example.weatherapp2.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherapp2.model.common.CityFullInfo

@Database(
    entities = [CityFullInfo::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun localDao(): LocalDao

    companion object {
        private var INSTANCE: DataBase? = null

        private val MIGRATION_1_2 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // database.execSQL("SELECT t2.name, t2.state, t2.country, t2.lat, t2.lon, t2.comment, t2.pic_uri, t1.current, t2.id FROM CityWeatherFullInfo as t1 JOIN CityCoordinate as t2 on t1.id = t2.id")
                database.execSQL(
                    "INSERT INTO CityFullInfo SELECT t2.name, t2.state, t2.country, t2.lat, t2.lon, t2.comment, t2.pic_uri, t1.current, t2.id FROM CityWeatherFullInfo as t1 JOIN CityCoordinate as t2 on t1.id = t2.id"
                )
                database.execSQL("DROP TABLE CityWeatherFullInfo")
                database.execSQL("DROP TABLE CityCoordinate")
            }
        }

        fun getDataBase(context: Context): DataBase? {
            if (INSTANCE == null) {
                synchronized(DataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataBase::class.java,
                        "myDB"
                    ).addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
