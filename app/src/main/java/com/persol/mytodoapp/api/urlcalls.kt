package com.persol.mytodoapp.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

suspend fun fetchDataFromApi(urlString: String): String? {
    val url = URL(urlString)
    val connection = withContext(Dispatchers.IO) {
        url.openConnection()
    }
    connection.doOutput = true
    withContext(Dispatchers.IO) {
        connection.connect()
    }

    return connection.inputStream.bufferedReader().use { it.readText() }
}

fun dataCall() {
    val apiUrl = "YOUR_API_URL"

    CoroutineScope(Dispatchers.IO).launch {

        val data = fetchDataFromApi(apiUrl)
        if (data != null) {

            println("Fetched data: $data")
        } else {
            println("Error fetching data from API")
        }
    }
}