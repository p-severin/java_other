package com.example.patry.kotlinweather

import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import com.example.patry.kotlinweather.models.MainWeather
import com.example.patry.kotlinweather.utilities.NetworkUtils
import com.google.gson.Gson
import java.io.IOException
import java.net.URL
import java.util.*

/**
 * Created by patry on 04.01.2018.
 */
class WeatherAPIQueryTask : AsyncTask<URL, Unit, String>() {

    var mListener : OnCustomEventListener? = null

    interface OnCustomEventListener{
        fun onEvent()
    }

    fun setCustomEventListener(eventListener: OnCustomEventListener) : Unit {
        mListener = eventListener
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg params: URL?): String {

        var searchURL : URL? = params[0]
        var weatherAPISearchResults : String? = null

        try {
            weatherAPISearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL!!)

        } catch (e : IOException){
            e.printStackTrace()
        }

        return weatherAPISearchResults!!
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        val gson = Gson()
        val weather = gson.fromJson(result, MainWeather::class.java)
    }

}