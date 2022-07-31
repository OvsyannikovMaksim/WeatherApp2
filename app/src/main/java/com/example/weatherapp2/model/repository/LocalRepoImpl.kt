package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.db.LocalDao

class LocalRepoImpl(private val localDao: LocalDao) : LocalRepo {

    override suspend fun getCitiesCoordinates(): List<CityCoordinate> {
        return localDao.getCitiesCoordinates()
    }

    override suspend fun getOneCity(id: Int): CityCoordinate? {
        return localDao.getOneCity(id)
    }

    override suspend fun addCityCoordinate(cityCoordinate: CityCoordinate) {
        localDao.addCityCoordinate(cityCoordinate)
    }

    override suspend fun updateCityCoordinate(cityCoordinate: CityCoordinate) {
        localDao.updateCityCoordinate(cityCoordinate)
    }

    override suspend fun deleteCityCoordinate(cityCoordinate: CityCoordinate) {
        localDao.deleteCityCoordinate(cityCoordinate)
    }

    override suspend fun getAllCityWeatherFullInfo(): List<CityWeatherFullInfo> {
        return localDao.getAllCityWeatherFullInfo()
    }

    override suspend fun getOneCityWeatherFullInfo(
        latitude: Double,
        longitude: Double
    ): CityWeatherFullInfo {
        return localDao.getOneCityWeatherFullInfo(latitude, longitude)
    }

    override suspend fun addOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo) {
        localDao.addOneCityWeatherFullInfo(cityWeatherFullInfo)
    }

    override suspend fun deleteOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo) {
        localDao.deleteOneCityWeatherFullInfo(cityWeatherFullInfo)
    }
}
