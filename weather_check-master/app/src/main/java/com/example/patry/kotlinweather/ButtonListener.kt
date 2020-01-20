package com.example.patry.kotlinweather

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by patry on 04.01.2018.
 */

class ButtonListener(context: MainActivity) : View.OnClickListener {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    val context = context
    val button : Button = context.locationControllerGPS
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationListenerGPS = MyLocationListener()


    override fun onClick(v: View?) {
        toggleGPSUpdates()
    }

    fun checkLocation() : Boolean {
        if (!isLocationEnabled()){
            showAlert()
        }
        return isLocationEnabled()
    }

    fun isLocationEnabled(): Boolean {

        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

    }

    fun toggleGPSUpdates() : Unit {
        if (!checkLocation()){
            return
        }

        if (button.text.equals(context.resources.getString(R.string.pause))){
            locationManager?.removeUpdates(locationListenerGPS)
        } else {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 1F, locationListenerGPS)
            button.setText(R.string.pause) //TODO Delete view dependency from here
        }
    }


    fun showAlert() : Unit {
        var dialog : AlertDialog.Builder = AlertDialog.Builder(context)


        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings",  DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    var myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(myIntent)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->

                })
        dialog.show()

    }




}