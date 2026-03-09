package com.ext.offlinerequestqueue.model

import java.util.UUID

data class QueuedRequest(

    val id: String = UUID.randomUUID().toString(),

    val url: String,

    val method: String,

    val body: String? = null,

    val headers: Map<String, String> = emptyMap(),

    var retryCount: Int = 0,

    val timestamp: Long = System.currentTimeMillis()
)