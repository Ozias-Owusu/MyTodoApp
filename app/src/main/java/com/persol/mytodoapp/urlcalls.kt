package com.persol.mytodoapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

suspend fun fetchDataFromApi(urlString: String): String? {
    val url = URL(urlString)
    val connection = url.openConnection()
    connection.doOutput = true
    connection.connect()

    return connection.inputStream.bufferedReader().use { it.readText() }
}

fun dataCall() {
    // Replace "YOUR_API_URL" with the actual URL of your API endpoint
    val apiUrl = "YOUR_API_URL"

    // Launch a coroutine to fetch data in a non-blocking way
    CoroutineScope(Dispatchers.IO).launch {
        val data = fetchDataFromApi(apiUrl)
        if (data != null) {
            // Process the fetched data (e.g., parse JSON, update UI)
            println("Fetched data: $data")
        } else {
            println("Error fetching data from API")
        }
    }
}