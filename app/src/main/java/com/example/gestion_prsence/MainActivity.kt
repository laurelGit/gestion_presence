package com.example.gestion_prsence

import Horaire
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gestion_prsence.ui.theme.Gestion_PrésenceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val horaire = Horaire()
    private val companyWifiMacAddress = "18:fd:74:3a:3b:17"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Gestion_PrésenceTheme {
                // Vos Composables ici
            }
        }

        auth = Firebase.auth // Initialisation de FirebaseAuth
    }

    override fun onStart() {
        super.onStart()
        signInWithEmailPassCustom()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            horaire.connecter(this, companyWifiMacAddress)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    horaire.connecter(this, companyWifiMacAddress)
                } else {
                    Toast.makeText(this, "Permission refusée, impossible de vérifier le WiFi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createUserWithEmailPassCustom() {
        auth.createUserWithEmailAndPassword("laurel@gmail.com", "password")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FirebaseAuth", "createUserWithEmail:success")
                    Toast.makeText(baseContext, "User created Successfully.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FirebaseAuth", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithEmailPassCustom() {
        auth.signInWithEmailAndPassword("laurel@gmail.com", "password")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FirebaseAuth", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Authentication successful. ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FirebaseAuth", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
