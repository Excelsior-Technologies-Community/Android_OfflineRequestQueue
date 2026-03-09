## Offline Request Queue Manager (Android Kotlin)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-orange)](#)

A lightweight **Android library** that automatically queues API requests when the device is offline and retries them when the network becomes available.

This library helps developers ensure **no API request is lost due to network issues**, making it ideal for apps that require reliable data syncing.

---

### Features

*  **Offline Request Queue** – Automatically store API requests when the device has no internet connection.
*  **Automatic Retry** – Queued requests are retried when the network becomes available.
*  **Persistent Storage** – Requests are stored locally using JSON so they survive app restarts.
*  **Lightweight** – No heavy dependencies like Room or Retrofit required.
*  **Multiple HTTP Methods** – Supports `GET`, `POST`, `PUT`, and `DELETE`.
*  **Easy Integration** – Simple API to enqueue requests from anywhere in your app.
*  **Safe Execution** – Handles exceptions and retry attempts safely.

---

## Installation

### Step 1: Add JitPack

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add Dependency

```gradle
dependencies {
	        implementation 'com.github.Excelsior-Technologies-Community:Android_SecureStorageHelper:1.0.0'
	}
```

---

## Getting Started

## 1. Initialize QueueManager

Initialize the library when your application starts.

Example in `Application` class:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        QueueManager.init(this)
    }
}
```

Register the application class in **AndroidManifest.xml**

```xml
<application
    android:name=".App"
    ...
/>
```

---

### Enqueue a Request

To queue an API request:

```kotlin
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

QueueManager.enqueue(context, request)
```

---

### How It Works

The library follows this workflow:

```
App sends API request
        ↓
Check internet connection
        ↓
If online → request executed immediately
If offline → request stored locally
        ↓
Worker retries queued requests
        ↓
Successful requests removed from queue
```

Queued requests are stored locally inside:

```
/data/data/<your_app_package>/files/offline_request_queue.json
```

---

### Testing Offline Mode

1. Disable internet connection
2. Trigger an API request using `enqueue()`
3. Request will be stored locally
4. Enable internet connection
5. Worker will retry and send the request

---

### Dependencies

This library uses minimal dependencies:

* **OkHttp** – Network requests
* **WorkManager** – Background retry
* **Gson** – JSON serialization

---

### License

```
MIT License

Copyright (c) 2025 Excelsior Technologies 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
