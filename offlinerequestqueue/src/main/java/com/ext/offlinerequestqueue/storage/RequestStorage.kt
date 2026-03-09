package com.ext.offlinerequestqueue.storage

import android.content.Context
import com.ext.offlinerequestqueue.model.QueuedRequest
import com.ext.offlinerequestqueue.utils.JsonUtil
import java.io.File

class RequestStorage(private val context: Context) {

    private val fileName = "offline_request_queue.json"

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun save(requests: List<QueuedRequest>) {

        val json = JsonUtil.toJson(requests)

        getFile().writeText(json)
    }

    fun load(): MutableList<QueuedRequest> {

        val file = getFile()

        if (!file.exists()) return mutableListOf()

        val json = file.readText()

        return JsonUtil.fromJson(json)
    }
}