package com.example.gestion_prsence

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Requete {

    private val db = FirebaseFirestore.getInstance()
//methode pour afficher une requete
    fun ajouterRequete(context:Context,requeteData: RqueteData) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.add(requeteData)
            .addOnSuccessListener {
                Toast.makeText(context, "Requete envoyée avec succès.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Echec de l'envoi de la requête.", Toast.LENGTH_SHORT).show()
            }
    }
//methode pour supprimer une requete ( reservée aux admins)
    fun supprimerRequete(documentId: String, context: Context) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.document(documentId).delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Requete supprimée avec succès.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Echec de la suppression de la requête.", Toast.LENGTH_SHORT).show()
            }
    }
//methode pour afficher les requetes ( reservée aux admins)
    fun afficherRequetes(context : Context) {
        val requetesCollection = db.collection("requetes")
        requetesCollection.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val requete = document.toObject(RqueteData::class.java)
                    // Traitez chaque requête ici (par exemple, afficher dans une RecyclerView)
                    println("Requete: ${requete}")
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Echec de l'affichage des requêtes.", Toast.LENGTH_SHORT).show()
            }
    }

}
