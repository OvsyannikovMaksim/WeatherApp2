package com.example.weatherapp2.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp2.model.common.CityFullInfo

@Dao
interface LocalDao : com.example.weatherapp2.model.repository.Dao {

    @Query("SELECT * FROM CityFullInfo")
    override fun dbUpdateLiveData(): LiveData<List<CityFullInfo>>

    @Query("SELECT * FROM CityFullInfo")
    override suspend fun getAllCityFullInfo(): List<CityFullInfo>

    @Query("SELECT * FROM CityFullInfo WHERE id =:id")
    override suspend fun getOneCityFullInfo(id: Int): CityFullInfo?

    @Query("SELECT * FROM CityFullInfo WHERE lon =:longitude and lat=:latitude")
    override suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo)

    @Update
    override suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo)

    @Query("DELETE FROM CityFullInfo WHERE id=:cityId")
    override suspend fun deleteCityFullInfo(cityId: Int)
}
