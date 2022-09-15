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
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun localDao(): LocalDao

    companion object {
        private var INSTANCE: DataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE CityFullInfo(name TEXT NOT NULL," +
                        "state TEXT, country TEXT NOT NULL, lat REAL NOT NULL, lon REAL NOT NULL," +
                        "comment TEXT, pic TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "'temp' REAL, feels_like REAL, pressure INTEGER, humidity INTEGER," +
                        "uvi REAL, wind_speed REAL, wind_deg INTEGER, weather TEXT)"
                )
                database.execSQL(
                    "INSERT INTO CityFullInfo SELECT t2.name, t2.state, t2.country, t2.lat," +
                        "t2.lon, t2.comment, t2.pic_uri, t2.id, t1.'temp', " +
                        "t1.feels_like, t1.pressure, t1.humidity, t1.uvi, t1.wind_speed," +
                        "t1.wind_deg, t1.weather FROM CityWeatherFullInfo as t1 " +
                        "JOIN CityCoordinate as t2 ON t1.id = t2.id"
                )
                database.execSQL("DROP TABLE CityWeatherFullInfo")
                database.execSQL("DROP TABLE CityCoordinate")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE CityFullInfo ADD COLUMN updateTime TEXT DEFAULT null "
                )
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
                        .addMigrations(MIGRATION_2_3)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
