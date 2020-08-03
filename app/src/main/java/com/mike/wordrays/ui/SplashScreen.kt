package com.mike.wordrays.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mike.wordrays.R
import com.mike.wordrays.data.PrefStore
import com.mike.wordrays.ui.admindashboard.AdminDashboard
import com.mike.wordrays.ui.home.Home
import com.mike.wordrays.ui.login.Login
import com.mike.wordrays.ui.register.AdminRegister
import com.mike.wordrays.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class SplashScreen : AppCompatActivity() {
    val prefStore: PrefStore by inject()
    var clickCounter = 0
    val auth = FirebaseAuth.getInstance()
    val databaseRef = FirebaseDatabase.getInstance().getReference("users")
    lateinit var authStateListener: AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authStateListener =
            AuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser == null) {
                    //Do anything here which needs to be done after signout is complete
                } else {
                    databaseRef.child(firebaseAuth.currentUser!!.uid).addValueEventListener(
                        object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Log.d("database", error.message)
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.child("admin").value == false) {
                                    startActivity(Intent(this@SplashScreen, Home::class.java))
                                    finish()
                                } else if (snapshot.child("admin").value == true){
                                    startActivity(Intent(this@SplashScreen, AdminDashboard::class.java))
                                    finish()
                                }
                            }
                        }
                    )
                }
            }

        auth.addAuthStateListener(authStateListener)

        Handler().postDelayed({
            setContentView(R.layout.activity_main)
            logo.setOnClickListener {
                clickCounter++
                if (clickCounter > 5) {
                    clickCounter = 0
                    //prefStore.logout()
                    startActivity(Intent(this, AdminRegister::class.java))
                }
            }

            login.setOnClickListener {
                startActivity(Intent(this, Login::class.java))
            }

            register.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }, 2000)

    }

    override fun onPause() {
        super.onPause()
        if (::authStateListener.isInitialized)auth.removeAuthStateListener(authStateListener)
    }
}
