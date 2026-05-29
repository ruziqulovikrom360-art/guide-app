package com.example.guidemetestapp.data.api

object UrlUtils {
    const val BASE_URL = "https://guide-me-8znn.onrender.com"

    fun getFullUrl(path: String?): String {
        if (path.isNullOrEmpty()) return "https://picsum.photos/400/300" 
        
        val cleanPath = path.replace("//", "/")
        return if (cleanPath.startsWith("/")) {
            "$BASE_URL$cleanPath"
        } else if (cleanPath.startsWith("http")) {
            cleanPath
        } else {
            "$BASE_URL/$cleanPath"
        }
    }
}
