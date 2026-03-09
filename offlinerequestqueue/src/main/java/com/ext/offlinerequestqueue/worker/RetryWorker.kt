package com.ext.offlinerequestqueue.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ext.offlinerequestqueue.core.QueueManager
import okhttp3.*

class RetryWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val client = OkHttpClient()

    override fun doWork(): Result {

        val queue = QueueManager.getQueue()

        val iterator = queue.iterator()

        while (iterator.hasNext()) {

            val requestData = iterator.next()

            try {

                val builder = Request.Builder()
                    .url(requestData.url)

                requestData.headers.forEach {
                    builder.addHeader(it.key, it.value)
                }

                if (requestData.method == "POST") {

                    val body = requestData.body
                        ?.toRequestBody(
                            "application/json".toMediaTypeOrNull()
                        )

                    builder.post(body!!)
                }

                val request = builder.build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {

                    QueueManager.remove(requestData)
                }

            } catch (e: Exception) {

                requestData.retryCount++
            }
        }

        return Result.success()
    }
}