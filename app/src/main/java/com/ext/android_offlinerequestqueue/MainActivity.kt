package com.ext.android_offlinerequestqueue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ext.offlinerequestqueue.core.QueueManager
import com.ext.offlinerequestqueue.model.QueuedRequest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        QueueManager.init(applicationContext)

        testQueue()
    }

    private fun testQueue() {

        val request = QueuedRequest(
            url = "https://jsonplaceholder.typicode.com/posts",
            method = "POST",
            body = """
                {
                  "title":"Offline Queue Test",
                  "body":"Hello World",
                  "userId":1
                }
            """.trimIndent()
        )

        QueueManager.enqueue(this, request)
    }
}