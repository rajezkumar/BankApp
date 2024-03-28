package com.raj.bankapp.di

import android.content.Context
import com.raj.bankapp.util.ApiUtil
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection

/**
 * A mock interceptor for Retrofit that intercepts outgoing requests and returns a pre-defined response.
 * The response is constructed using a JSON file loaded from local, simulating a real API response.
 */

class MockRequestInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().let {
            val jsonContent = ApiUtil.readJsonFromFile(context)
            return Response.Builder()
                .request(it)
                .protocol(Protocol.HTTP_2)
                .code(HttpURLConnection.HTTP_OK)
                .message("message")
                .body(jsonContent?.toResponseBody("application/json".toMediaType()))
                .build()
        }
    }
}