package com.example.gestion_prsence

import android.content.ContentValues.TAG
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var employee: Employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Aut
        auth = FirebaseAuth.getInstance()
        employee = Employee()

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
    public override fun onStart() {
        super.onStart()
     // employee.createUserWithEmailPassCustom(auth, "laurel", "laurel16@gmail.com","authentification","employe")
       employee.signInWithEmailPassCustom(auth, "laurel16@gmail.com", "authentification")

      Toast.makeText(baseContext, "User info : " + auth.currentUser?.email + " - " + auth.currentUser?.displayName,
      Toast.LENGTH_LONG).show()
  //  employee.updateCurrentUser(this,"nadine","password")
       // employee.updateUserRole(this,"laurel16@gmail.com","administrateur")
     //employee.deleteUser()
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