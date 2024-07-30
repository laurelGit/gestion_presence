package com.example.gestion_prsence

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Reseau(private val activity: Activity) {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    fun getwifiMacAdresse(context: Context, companyWifiMacAddress: String) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        if (wifiInfo != null && wifiInfo.networkId != -1) {
            val currentWifiMacAddress: String = wifiInfo.bssid
            val wifiSSID = wifiInfo.ssid

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Requires ACCESS_FINE_LOCATION permission
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not already granted
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                    return
                }
            }

            // Display WiFi SSID and MAC address
            Toast.makeText(context, "Nom du WiFi : $wifiSSID\nAdresse MAC : $currentWifiMacAddress", Toast.LENGTH_LONG).show()

            if (currentWifiMacAddress.equals(companyWifiMacAddress, ignoreCase = true)) {
                Toast.makeText(context, "Vous êtes connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
                // MarquerPresence(context)
            } else {
                Toast.makeText(context, "Vous n'êtes pas connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Aucun WiFi actif détecté", Toast.LENGTH_LONG).show()
        }
    }

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getwifiMacAdresse(activity, "57.1u.6f.41.14.a1") // Replace with actual WiFi MAC address
        }
    }
}
