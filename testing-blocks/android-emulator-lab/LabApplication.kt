package com.inkr8.lab

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions

// Test harness only. No product configuration or Google OAuth implementation.
class LabApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val app = FirebaseApp.getInstance()
        check(app.options.projectId == "demo-inkr8-local")
        check(packageName == "com.inkr8.lab")
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
        FirebaseAuth.getInstance().useEmulator("127.0.0.1", 9099)
        FirebaseFirestore.getInstance().useEmulator("127.0.0.1", 8080)
        FirebaseFunctions.getInstance().useEmulator("127.0.0.1", 5001)
    }
}
