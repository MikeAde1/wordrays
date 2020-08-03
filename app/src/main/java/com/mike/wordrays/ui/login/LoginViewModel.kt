package com.mike.wordrays.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mike.wordrays.data.Resource
import com.mike.wordrays.utils.isValidEmail

class LoginViewModel: ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val errorEntry = MutableLiveData<String>()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val status = MutableLiveData<Resource<String>>()
    val databaseRef = FirebaseDatabase.getInstance().getReference("users")

    fun loginUser() {
        if (validateFields()) {
            status.value  = Resource.loading()
            firebaseAuth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) {
                            databaseRef.child(firebaseUser.uid).addValueEventListener(
                                    object : ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("database", error.message)
                                            status.value = Resource.error(it.exception?.message?:"Login failed!", null)
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.hasChild("admin") && snapshot.child("admin").value == true) {
                                                status.value = Resource.success(null, "admin")
                                            } else if(snapshot.hasChild("first_name")) {
                                                status.value = Resource.success(null, "filled")
                                            } else {
                                                status.value = Resource.success(null, null)
                                            }
                                        }
                                    }
                                )
                        } else {
                            Log.d("database", it.exception?.message?:"Error User")
                            status.value = Resource.error(it.exception?.message?:"Login failed!",null)
                        }
                    } else {
                        Log.d("database", it.exception?.message?:"Error Not successful")
                        status.value = Resource.error(it.exception?.message?:"Login failed!",null)
                    }
                }.addOnFailureListener {
                    Log.d("database", it.message?:"Error failure")
                    status.value = Resource.error(it.message?:"Login failed!",null )
                }
        }
    }

    private fun validateFields(): Boolean {
        if (email.value.isNullOrBlank()) {
            errorEntry.value = "Email cannot be blank"
            return false
        }
        if (!email.value.isValidEmail()) {
            errorEntry.value = "Enter a valid Email"
            return false
        }
        if (password.value.isNullOrBlank()) {
            errorEntry.value = "Password cannot be blank"
            return false
        }
        return true
    }
}