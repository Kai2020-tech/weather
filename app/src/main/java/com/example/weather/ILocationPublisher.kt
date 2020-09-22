package com.example.weather

interface ILocationPublisher {
    fun getCurrentLocationWeatherRecord(): WeatherData.Records.Location?
    fun add(subscriber: () -> Unit)
    fun remove(subscriber: () -> Unit)
}