package com.example.weatherapp2.model.common

import android.content.Context

interface Mapper<Src, Dst> {
    fun map(src: Src, context: Context): Dst
}
