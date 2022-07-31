package com.example.weatherapp2.model.db

import androidx.room.*
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

@Dao
interface LocalDao {

    // Это для координат городов
    @Query("SELECT * FROM CityCoordinate")
    suspend fun getCitiesCoordinates(): List<CityCoordinate>

    @Query("SELECT * FROM CityCoordinate WHERE id =:id")
    suspend fun getOneCity(id: Int): CityCoordinate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityCoordinate(cityCoordinate: CityCoordinate)

    @Update
    suspend fun updateCityCoordinate(cityCoordinate: CityCoordinate)

    @Delete
    suspend fun deleteCityCoordinate(cityCoordinate: CityCoordinate)

    // Это для погоды
    @Query("SELECT * FROM CityWeatherFullInfo")
    suspend fun getAllCityWeatherFullInfo(): List<CityWeatherFullInfo>

    @Query("SELECT * FROM CityWeatherFullInfo WHERE lon =:longitude and lat=:latitude")
    suspend fun getOneCityWeatherFullInfo(latitude: Double, longitude: Double): CityWeatherFullInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo)

    @Delete
    suspend fun deleteOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo)
}
