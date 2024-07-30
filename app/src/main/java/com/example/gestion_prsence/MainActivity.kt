package com.example.gestion_prsence

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gestion_prsence.model.Employee
import com.example.gestion_prsence.ui.theme.Gestion_PrésenceTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var employee: Employee
    private val reseau = Reseau(this)
    private val wifi = "57.1u.6f.41.14.a1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        employee = Employee()
        reseau.checkPermissions()

        enableEdgeToEdge()
        setContent {
            Gestion_PrésenceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, call the method getwifiMacAdresse
                    reseau.getwifiMacAdresse(this, wifi)
                } else {
                    // Permission denied, display a message or handle the case
                    Toast.makeText(this, "Permission denied, cannot check WiFi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // employee.createUserWithEmailPassCustom(auth, "laurel1@gmail.com", "password")
        employee.signInWithEmailPassCustom(auth, "laurel1@gmail.com", "password")

        Toast.makeText(baseContext, "User info : " + auth.currentUser?.email + " - " + auth.currentUser?.displayName, Toast.LENGTH_LONG).show()
        // employee.updateProfil()
        // employee.deleteUser()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gestion_PrésenceTheme {
        Greeting("Android")
    }
}
