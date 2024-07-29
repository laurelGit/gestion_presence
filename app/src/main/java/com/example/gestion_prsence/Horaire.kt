import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Horaire {
    private val db = FirebaseFirestore.getInstance()

    fun connecter(context: Context, companyWifiMacAddress: String) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        if (wifiInfo != null && wifiInfo.networkId != -1) {
            val currentWifiMacAddress: String
            val wifiSSID = wifiInfo.ssid

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Requires ACCESS_FINE_LOCATION permission
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not already granted
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    return
                }
                currentWifiMacAddress = wifiInfo.bssid
            } else {
                currentWifiMacAddress = wifiInfo.bssid
            }

            // Display WiFi SSID and MAC address
            Toast.makeText(context, "Nom du WiFi : $wifiSSID\nAdresse MAC : $currentWifiMacAddress", Toast.LENGTH_LONG).show()

            if (currentWifiMacAddress.equals(companyWifiMacAddress, ignoreCase = true)) {
                Toast.makeText(context, "Vous êtes connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
                marquerArrive(context)
            } else {
                Toast.makeText(context, "Vous n'êtes pas connecté au WiFi de l'entreprise", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Aucun WiFi actif détecté", Toast.LENGTH_LONG).show()
        }
    }


    fun marquerArrive(context: Context) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val heure = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (email == null) {
            Toast.makeText(context, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
            return
        }

        val presenceData = mapOf(
            "arrive" to heure,
            "depart" to null,
            "date" to date,
            "email" to email
        )

        db.collection("users").document(email)
            .set(presenceData)
            .addOnSuccessListener {
                Toast.makeText(context, "Heure d'arrivée enregistrée avec succès", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erreur lors de l'enregistrement de l'heure d'arrivée", Toast.LENGTH_SHORT).show()
            }
    }

    fun marquerDepart(context: Context) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val heure = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        if (email == null) {
            Toast.makeText(context, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users").document(email)
            .update("depart", heure)
            .addOnSuccessListener {
                Toast.makeText(context, "Heure de départ enregistrée avec succès", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erreur lors de l'enregistrement de l'heure de départ", Toast.LENGTH_SHORT).show()
            }
    }
}
