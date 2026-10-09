package com.inkr8.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import com.google.firebase.auth.FirebaseAuth
import com.inkr8.repository.UserRepository
import com.inkr8.data.Users
import com.inkr8.AppRoot
import com.inkr8.ui.theme.Inkr8Theme

// Hosts the unchanged production composables; it bypasses MainActivity/Ads/OAuth.
class LabHostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instrumentation supplies its own host content; the APK also has a usable local entry.
        show {
            var user by remember { mutableStateOf<Users?>(null) }
            var error by remember { mutableStateOf<String?>(null) }
            val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
            if (user != null) AppRoot(user!!,launcher,{user=null}) else Column {
                Text("Laboratorio demo-inkr8-local")
                Text("Auth anónimo local; R8 HTTP simulado. OAuth y Ads no comprobados.")
                Button(onClick={
                    FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener { result ->
                        UserRepository().ensureUserExists(result.user!!.uid,null) { user=it }
                    }.addOnFailureListener { error=it.message }
                }) {Text("Entrar al laboratorio local")}
                error?.let {Text(it)}
            }
        }
    }
    fun show(content: @Composable () -> Unit) {
        setContent { Inkr8Theme { content() } }
    }
}
