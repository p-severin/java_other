package com.example.patry.kotlinweather.utilities

import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by patry on 29.12.2017.
 */
object NetworkUtils {

    val WEATHER_API_BASE_URL : String = "https://api.openweathermap.org/data/2.5/weather"
    val PARAM_QUERY_LAT : String = "lat"
    val PARAM_QUERY_LON : String = "lon"
    val PARAM_QUERY_APP_ID : String = "appid"
    val API_KEY : String = "34e4c014bc3013ec4dc84b4450334170"

    fun buildUrl(longitudePosition : String, latitudePosition: String) : URL {

        var builtUri : Uri = Uri.parse(WEATHER_API_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY_LAT, latitudePosition)
                .appendQueryParameter(PARAM_QUERY_LON, longitudePosition)
                .appendQueryParameter(PARAM_QUERY_APP_ID, API_KEY)
                .build()

        println(builtUri.toString())

        var url : URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e : MalformedURLException){
            e.printStackTrace()
        }

        return url!!
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun getResponseFromHttpUrl(url : URL) : String? {

        var urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection

        try {

            var inputStream : InputStream = urlConnection.inputStream

            if (inputStream != null){
                var bufferedReader : BufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line : String = ""
                var result : String = ""
                for (line in bufferedReader.lines()){
                    result += line
                    result += "\n"
                }
                inputStream.close()
                println(result)
                return result
            }

        } finally {
            urlConnection.disconnect()
        }

        return null
    }

}