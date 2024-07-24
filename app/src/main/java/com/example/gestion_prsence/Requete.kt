package com.example.gestion_prsence

import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Requete {

    private val db = FirebaseFirestore.getInstance()

    fun ajouterRequete(
        requeteData: RqueteData,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.add(requeteData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    fun supprimerRequete(
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.document(documentId).delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
