package com.ext.offlinerequestqueue.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ext.offlinerequestqueue.core.QueueManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RetryWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val client = OkHttpClient()

    override fun doWork(): Result {

        val queue = QueueManager.getQueue().toList() // Safe copy

        for (requestData in queue) {

            try {

                val builder = Request.Builder()
                    .url(requestData.url)

                // Add headers
                requestData.headers.forEach { (key, value) ->
                    builder.addHeader(key, value)
                }

                // Handle HTTP methods
                when (requestData.method.uppercase()) {

                    "POST" -> {

                        val body = requestData.body?.toRequestBody(
                            "application/json".toMediaTypeOrNull()
                        )

                        if (body != null) {
                            builder.post(body)
                        }
                    }

                    "PUT" -> {

                        val body = requestData.body?.toRequestBody(
                            "application/json".toMediaTypeOrNull()
                        )

                        if (body != null) {
                            builder.put(body)
                        }
                    }

                    "DELETE" -> builder.delete()

                    else -> builder.get()
                }

                val request = builder.build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {

                    QueueManager.remove(requestData)

                } else {

                    requestData.retryCount++
                }

                response.close()

            } catch (e: Exception) {

                requestData.retryCount++
            }
        }

        return Result.success()
    }
}