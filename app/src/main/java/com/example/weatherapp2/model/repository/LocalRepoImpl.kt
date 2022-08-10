package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.LocalDao

class LocalRepoImpl(private val localDao: LocalDao) : LocalRepo {

    override suspend fun getAllCityFullInfo(): List<CityFullInfo> {
        return localDao.getAllCityFullInfo()
    }

    override suspend fun getOneCityFullInfo(id: Int): CityFullInfo? {
        return localDao.getOneCityFullInfo(id)
    }

    override suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo? {
        return localDao.getOneCityFullInfo(latitude, longitude)
    }

    override suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo) {
        localDao.insertCityFullInfo(cityFullInfo)
    }

    override suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo) {
        localDao.updateCityFullInfo(cityFullInfo)
    }

    override suspend fun deleteCityFullInfo(cityFullInfo: CityFullInfo) {
        localDao.deleteCityFullInfo(cityFullInfo)
    }
}
