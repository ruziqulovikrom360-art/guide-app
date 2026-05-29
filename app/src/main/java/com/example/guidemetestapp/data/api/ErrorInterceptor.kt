package com.example.guidemetestapp.data.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            showToast("Network error: ${e.message}")
            throw e
        }

        if (!response.isSuccessful) {
            val errorMsg = when (response.code) {
                401 -> "Unauthorized. Please login again."
                404 -> "Resource not found."
                422 -> "Validation error."
                500 -> "Server error. Please try again later."
                else -> "Error: ${response.code} ${response.message}"
            }
            showToast(errorMsg)
        }

        return response
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
