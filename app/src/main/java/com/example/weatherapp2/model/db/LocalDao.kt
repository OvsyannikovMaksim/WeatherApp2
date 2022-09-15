package com.example.weatherapp2.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp2.model.common.CityFullInfo

@Dao
interface LocalDao {

    @Query("SELECT * FROM CityFullInfo")
    fun dbUpdateLiveData(): LiveData<List<CityFullInfo>>

    @Query("SELECT * FROM CityFullInfo")
    suspend fun getAllCityFullInfo(): List<CityFullInfo>

    @Query("SELECT * FROM CityFullInfo WHERE id =:id")
    suspend fun getOneCityFullInfo(id: Int): CityFullInfo?

    @Query("SELECT * FROM CityFullInfo WHERE lon =:longitude and lat=:latitude")
    suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo)

    @Update
    suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo)

    @Delete
    suspend fun deleteCityFullInfo(cityFullInfo: CityFullInfo)
}
