package com.example.gestion_prsence.model

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import android.content.Context

class Employee : ComponentActivity() {
    private var name: String = ""
    private var email: String = ""

    public fun getName(): String {
        return name
    }

    public fun getEmail(): String {
        return email
    }

    public fun createUserWithEmailPassCustom(auth: FirebaseAuth, Email: String,password: String){
        auth.createUserWithEmailAndPassword(Email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                }
            }
    }
    public fun signInWithEmailPassCustom(auth: FirebaseAuth, currentEmail: String, password: String){
        auth.signInWithEmailAndPassword(currentEmail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        readUser(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                }
            }
    }
    private fun readUser(user: FirebaseUser) {
        this.name = user.displayName.toString()
        this.email = user.email.toString()
    }
  /*  public fun updateUser(){
        val user = Firebase.auth.currentUser
        val newPassword = "other"

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
                else{
                    Log.d(TAG, "User password not updated.")
                }
            }
    }*/
    public fun updateProfil(){
      val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            val newPassword = "password"
            //  photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }
    public fun deleteUser(){
       val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }
}
