package com.ext.offlinerequestqueue.utils

import com.ext.offlinerequestqueue.model.QueuedRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtil {

    private val gson = Gson()

    fun toJson(requests: List<QueuedRequest>): String {
        return gson.toJson(requests)
    }

    fun fromJson(json: String): MutableList<QueuedRequest> {

        val type = object : TypeToken<MutableList<QueuedRequest>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }
}