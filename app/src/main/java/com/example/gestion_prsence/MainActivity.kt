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
import com.example.gestion_prsence.ui.theme.Gestion_PrésenceTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
   // private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



        // Initialize Firebase Auth
        auth = Firebase.auth
    }
    public override fun onStart() {
        super.onStart()
        val requeteData = RqueteData(
            email = "ajeangael@gmail.com",
            motif = "retard",
            detail = "la pluie m'a bloquée le matin le matin",
             heure = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
             date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )

        val requete = Requete()
        requete.ajouterRequete(requeteData,
            onSuccess = {
                Log.d("succes","requete envoyée avec succes")
                Toast.makeText(this, "Requête ajoutée avec succès", Toast.LENGTH_SHORT).show()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Erreur lors de l'ajout de la requête: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.d("echec","echec d'envoie de la requete")
            }
        )
    }

    private fun createUserWithEmailPassCustom(){
        auth.createUserWithEmailAndPassword("laurel@gmail.com", "password")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "User created Successfully.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    private fun signInWithEmailPassCustom(){
        auth.signInWithEmailAndPassword("laurel@gmail.com", "password")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Authentication succesfull. " + auth.currentUser?.email,
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
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