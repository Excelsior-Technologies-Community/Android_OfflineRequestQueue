package com.ext.offlinerequestqueue.core

import android.content.Context
import androidx.work.*
import com.ext.offlinerequestqueue.model.QueuedRequest
import com.ext.offlinerequestqueue.network.NetworkMonitor
import com.ext.offlinerequestqueue.storage.RequestStorage
import com.ext.offlinerequestqueue.worker.RetryWorker
import java.util.concurrent.TimeUnit

object QueueManager {

    private lateinit var storage: RequestStorage

    private val queue = mutableListOf<QueuedRequest>()

    fun init(context: Context) {

        storage = RequestStorage(context)

        queue.addAll(storage.load())
    }

    fun enqueue(context: Context, request: QueuedRequest) {

        if (NetworkMonitor.isInternetAvailable(context)) {

            scheduleRetry(context)

        } else {

            queue.add(request)

            storage.save(queue)
        }
    }

    fun getQueue(): MutableList<QueuedRequest> {
        return queue
    }

    fun remove(request: QueuedRequest) {

        queue.remove(request)

        storage.save(queue)
    }

    private fun scheduleRetry(context: Context) {

        val workRequest =
            OneTimeWorkRequestBuilder<RetryWorker>()
                .setInitialDelay(2, TimeUnit.SECONDS)
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(workRequest)
    }
}