package com.example.gestion_prsence

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Requete {

    private val db = FirebaseFirestore.getInstance()

    fun ajouterRequete(
        requeteData: RqueteData , context: Context) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.add(requeteData)
            .addOnSuccessListener {
                Toast.makeText(context, " Requete Envoyée avec succès", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erreur l'or de l'envoie de de la requte", Toast.LENGTH_SHORT).show()
            }
    }
    fun supprimerRequete(
        documentId: String, context: Context) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.document(documentId).delete()
            .addOnSuccessListener {
                Toast.makeText(context, " Requete Supprimée avec succès", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erreur l'or de la suppression de la requte", Toast.LENGTH_SHORT).show()
            }
            }
}
