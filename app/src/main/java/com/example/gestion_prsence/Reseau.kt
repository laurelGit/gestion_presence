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
    private val LOCATION_PERMISSION_REQUEST_CODE = 3

    // Vérifier et demander les permissions nécessaires
    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getwifiMacAdresse("57:1u:6f:41:14:a1") // Remplacer par l'adresse MAC WiFi réelle
        }
    }

    // Obtenir l'adresse MAC du WiFi connecté
    fun getwifiMacAdresse(companyWifiMacAddress: String) {
        val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        if (wifiInfo != null && wifiInfo.networkId != -1) {
            val currentWifiMacAddress: String = wifiInfo.bssid
            val wifiSSID = wifiInfo.ssid

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Requiert la permission ACCESS_FINE_LOCATION
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Demander la permission si elle n'est pas déjà accordée
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                    return
                }
            }

            // Afficher le SSID et l'adresse MAC du WiFi connecté
            Toast.makeText(activity, "Nom du WiFi : $wifiSSID\nAdresse MAC : $currentWifiMacAddress", Toast.LENGTH_LONG).show()

            if (currentWifiMacAddress.equals(companyWifiMacAddress, ignoreCase = true)) {
                Toast.makeText(activity, "Vous êtes connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
                // Marquer la présence (décommenter et implémenter cette méthode si nécessaire)
                // MarquerPresence(context)
            } else {
                Toast.makeText(activity, "Vous n'êtes pas connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(activity, "Aucun WiFi actif détecté", Toast.LENGTH_LONG).show()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getwifiMacAdresse("57:1u:6f:41:14:a1") // Remplacer par l'adresse MAC WiFi réelle
                } else {
                    Toast.makeText(activity, "Permission denied, cannot check WiFi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
