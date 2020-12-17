package com.example.weather

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("records")
    val records: Records = Records(),
    @SerializedName("result")
    val result: Result = Result(),
    @SerializedName("success")
    val success: String = "" // true
) {
    data class Records(
        @SerializedName("datasetDescription")
        val datasetDescription: String = "", // 三十六小時天氣預報
        @SerializedName("location")
        val location: List<Location> = listOf()
    ) {
        data class Location(
            @SerializedName("locationName")
            val locationName: String = "", // 嘉義縣
            @SerializedName("weatherElement")
            val weatherElement: List<WeatherElement> = listOf()
        ) {
            data class WeatherElement(
                @SerializedName("elementName")
                val elementName: String = "", // Wx
                @SerializedName("time")
                val time: List<Time> = listOf()
            ) {
                data class Time(
                    @SerializedName("endTime")
                    val endTime: String = "", // 2020-08-07 18:00:00
                    @SerializedName("parameter")
                    val parameter: Parameter = Parameter(),
                    @SerializedName("startTime")
                    val startTime: String = "" // 2020-08-07 12:00:00
                ) {
                    data class Parameter(
                        @SerializedName("parameterName")
                        val parameterName: String = "", // 多雲午後短暫雷陣雨
                        @SerializedName("parameterUnit")
                        val parameterUnit: String = "", // 百分比
                        @SerializedName("parameterValue")
                        val parameterValue: String = "" // 22
                    )
                }
            }
        }
    }

    data class Result(
        @SerializedName("fields")
        val fields: List<Field> = listOf(),
        @SerializedName("resource_id")
        val resourceId: String = "" // F-C0032-001
    ) {
        data class Field(
            @SerializedName("id")
            val id: String = "", // datasetDescription
            @SerializedName("type")
            val type: String = "" // String
        )
    }
}
