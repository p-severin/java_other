package com.example.patry.kotlinweather

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.example.patry.kotlinweather.utilities.NetworkUtils
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity(), EventListener {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationControllerGPS.setOnClickListener(ButtonListener(this))

        setCustomEventListener

    }

    fun updateUI(){

    }




}
