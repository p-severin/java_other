package com.example.patry.kotlinweather

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import com.example.patry.kotlinweather.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


/**
 * Created by patry on 29.12.2017.
 */
class MyLocationListener : LocationListener {

    override fun onLocationChanged(location: Location) {

        val longitudeAsString = (Math.floor(location.longitude*1000)/1000).toString()
        val latitudeAsString = (Math.floor(location.latitude*1000)/1000).toString()

        val url = NetworkUtils.buildUrl(longitudeAsString, latitudeAsString)
        WeatherAPIQueryTask().execute(url)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}