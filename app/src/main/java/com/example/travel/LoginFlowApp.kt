package com.example.travel

import android.app.Application
import com.google.firebase.FirebaseApp
// TODO: Add Gmail login
// TODO: Add success or error notifications
// TODO: Create better restrictions for password (maybe add notifications for that)
// TODO: Replace the logo with a better one

class LoginFlowApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}