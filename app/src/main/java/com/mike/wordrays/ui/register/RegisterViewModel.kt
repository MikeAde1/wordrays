package com.mike.wordrays.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mike.wordrays.data.Resource
import com.mike.wordrays.utils.isValidEmail


class RegisterViewModel: ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val errorEntry = MutableLiveData<String>()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val status = MutableLiveData<Resource<String>>()

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

    fun createUser() {
        if (validateFields()) {
            status.value = Resource.loading()
            firebaseAuth.createUserWithEmailAndPassword(
                email.value.toString(),
                password.value.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user: FirebaseUser? = firebaseAuth.currentUser
                        status.value = Resource.success(null, null)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("firebase", task.exception.toString()+ task.exception?.message)
                        status.value = Resource.error(task.exception?.message ?: "Registration failed!", null)
                    }

                }
        }
    }
}