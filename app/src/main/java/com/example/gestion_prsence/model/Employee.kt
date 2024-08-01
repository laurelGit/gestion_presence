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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class Employee: ComponentActivity() {
    private var name: String = ""
    private var email: String = ""

    public fun getName(): String {
        return name
    }

    public fun getEmail(): String {
        return email
    }

    fun createUserWithEmailPassCustom(
        auth: FirebaseAuth,
        name: String,
        mail: String,
        password: String,
        roleN: String
    ) {
        auth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val db = Firebase.firestore
                    val employeeRef = db.collection("employees").document()
                    val role = role_employes.fromName(roleN)

                    if (role == null) {
                        // Role is not valid
                        Log.d(TAG, "Invalid role name")
                        auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                Log.d(TAG, "User deleted due to invalid role")
                            } else {
                                Log.d(TAG, "Failed to delete user: ${deleteTask.exception?.message}")
                            }
                        }
                        return@addOnCompleteListener
                    }
                    val employeeData = mapOf(
                        "name" to name,
                        "email" to mail,
                        "password" to password,
                        "role" to mapOf(
                            "id" to role.id,
                            "name" to role.name
                        )
                    )

                    employeeRef.set(employeeData)
                        .addOnSuccessListener {
                            Log.d(TAG, "User registered successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Failed to add user to Firestore: ${e.message}")
                        }
                } else {
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
    public fun updateCurrentUser(
        context: Context,
        name: String?,
        password: String?
    ) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val db = Firebase.firestore
            val userEmail = currentUser.email ?: return

            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            // Mise à jour du profil (nom)
            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Profile updated successfully")

                        // Si un mot de passe est fourni, le mettre à jour dans Firebase Authentication
                        if (password != null) {
                            currentUser.updatePassword(password)
                                .addOnCompleteListener { passwordTask ->
                                    if (passwordTask.isSuccessful) {
                                        Log.d(TAG, "Password updated successfully in Firebase Authentication")

                                        // Ré-authentification avec le nouveau mot de passe
                                        auth.signInWithEmailAndPassword(userEmail, password)
                                            .addOnCompleteListener { signInTask ->
                                                if (signInTask.isSuccessful) {
                                                    Log.d(TAG, "Re-authentication successful with new password")

                                                    // Mise à jour des données dans Firestore
                                                    val employeesRef = db.collection("employees")
                                                    employeesRef.whereEqualTo("email", userEmail).get()
                                                        .addOnSuccessListener { querySnapshot ->
                                                            if (querySnapshot.isEmpty) {
                                                                Log.w(TAG, "No document found for email: $userEmail")
                                                            } else {
                                                                val document = querySnapshot.documents.first()
                                                                val documentId = document.id
                                                                val employeeRef = employeesRef.document(documentId)
                                                                val updates = mutableMapOf<String, Any>()

                                                                name?.let { updates["name"] = it }
                                                                password?.let { updates["password"] = it }

                                                                if (updates.isNotEmpty()) {
                                                                    employeeRef.update(updates)
                                                                        .addOnSuccessListener {
                                                                            Log.d(TAG, "User updated successfully in Firestore")
                                                                        }
                                                                        .addOnFailureListener { e ->
                                                                            Log.e(TAG, "Failed to update user in Firestore", e)
                                                                        }
                                                                } else {
                                                                    Log.w(TAG, "No updates to apply")
                                                                }
                                                            }
                                                        }
                                                        .addOnFailureListener { e ->
                                                            Log.e(TAG, "Failed to fetch documents", e)
                                                        }
                                                } else {
                                                    Log.e(TAG, "Re-authentication failed", signInTask.exception)
                                                }
                                            }
                                    } else {
                                        Log.e(TAG, "Failed to update password in Firebase Authentication", passwordTask.exception)
                                    }
                                }
                        } else {
                            // Mise à jour des données dans Firestore si le mot de passe n'est pas fourni
                            val employeesRef = db.collection("employees")
                            employeesRef.whereEqualTo("email", userEmail).get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (querySnapshot.isEmpty) {
                                        Log.w(TAG, "No document found for email: $userEmail")
                                    } else {
                                        val document = querySnapshot.documents.first()
                                        val documentId = document.id
                                        val employeeRef = employeesRef.document(documentId)
                                        val updates = mutableMapOf<String, Any>()

                                        name?.let { updates["name"] = it }

                                        if (updates.isNotEmpty()) {
                                            employeeRef.update(updates)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "User updated successfully in Firestore")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e(TAG, "Failed to update user in Firestore", e)
                                                }
                                        } else {
                                            Log.w(TAG, "No updates to apply")
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to fetch documents", e)
                                }
                        }
                    } else {
                        Log.e(TAG, "Profile update failed", task.exception)
                    }
                }
        } else {
            Log.w(TAG, "No authenticated user")
        }
    }

    public fun updateUserRole(
        context: Context,
        userEmail: String,
        newRoleName: String
    ) {
        val db = Firebase.firestore
        val employeesRef = db.collection("employees")

        // Convertir le nom du rôle en type enum
        val newRole = role_employes.fromName(newRoleName)
        if (newRole == null) {
            Log.d(TAG, "Invalid role name: $newRoleName")
            return
        }

        employeesRef.whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Log.w(TAG, "No document found for email: $userEmail")
                    Toast.makeText(context, "No document found for email: $userEmail", Toast.LENGTH_SHORT).show()
                } else {
                    val document = querySnapshot.documents.first()
                    val documentId = document.id
                    val employeeRef = employeesRef.document(documentId)

                    val updates = mapOf(
                        "role" to mapOf(
                            "id" to newRole.id,
                            "name" to newRole.name
                        )
                    )

                    employeeRef.update(updates)
                        .addOnSuccessListener {
                            Log.d(TAG, "User role updated successfully")
                            Toast.makeText(context, "User role updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Failed to update user role in Firestore", e)
                            Toast.makeText(context, "Failed to update user role", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch documents", e)
                Toast.makeText(context, "Failed to fetch documents", Toast.LENGTH_SHORT).show()
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
