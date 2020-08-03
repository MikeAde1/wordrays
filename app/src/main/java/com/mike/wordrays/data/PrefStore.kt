package com.mike.wordrays.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mike.wordrays.ui.SplashScreen
import com.mike.wordrays.ui.register.RegisterActivity

const val SHARED_PREF = "shared_pref"
const val USER = "user"

class PrefStore(private val context: Context) {
    private val sharedPref: SharedPreferences by lazy { context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE) }
    private val editor: SharedPreferences.Editor = sharedPref.edit()
    private val gson = Gson()

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        editor.clear()
        editor.apply()
        val i = Intent(context, SplashScreen::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }

    fun saveLogIn(status: Boolean) {
        editor.putBoolean("logged_in", status).apply()
    }

    fun isLoggedIn(): Boolean{
        return sharedPref.getBoolean("logged_in", false)
    }
}