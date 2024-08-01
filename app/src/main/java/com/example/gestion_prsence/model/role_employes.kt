package com.example.gestion_prsence.model
enum class role_employes(val id: Int, val nameR: String) {
    ADMINISTRATEUR(1, "Administrateur"),
    EMPLOYE(2, "Employe");

    companion object {
        fun fromName(displayName: String): role_employes? {
            return values().find { it.nameR.equals(displayName, ignoreCase = true) }
        }
    }
}
