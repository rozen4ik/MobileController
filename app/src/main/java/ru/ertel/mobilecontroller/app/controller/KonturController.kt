package ru.ertel.mobilecontroller.app.controller

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class KonturController {
    fun requestPOST(url: String, content: String): String {
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType = "application/xml; charset=windows-1251".toMediaType()
        val body: RequestBody = content.toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/xml; charset=Windows-1251")
            .addHeader("Content-Encoding", "Windows-1251")
            .build()
        val response = client.newCall(request).execute()
        val str = response.body?.string()
        return str.toString()
    }
}