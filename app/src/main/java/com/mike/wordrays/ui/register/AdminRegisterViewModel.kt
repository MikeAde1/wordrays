package com.mike.wordrays.ui.register

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.mike.wordrays.data.Resource
import com.mike.wordrays.utils.isValidEmail

class AdminRegisterViewModel: ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val errorEntry = MutableLiveData<String>()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val status = MutableLiveData<Resource<String>>()
    val dataBaseRef = FirebaseDatabase.getInstance().getReference("users")

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

    fun createAdmin() {
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
                        setAdminStatus(user?.uid)
                    } else {
                        // If sign in fails, display a message to the user.
                        status.value =
                            Resource.error(task.exception?.message ?: "Registration failed!",null)
                    }

                }
        }
    }

    private fun setAdminStatus(userId: String?) {
        dataBaseRef.child(userId!!).setValue(
            mapOf(Pair("admin", true), Pair("user_id", userId))
        ).addOnCompleteListener {
            if (it.isComplete) {
                status.value = Resource.success(null, null)
            } else {
                status.value = Resource.error("Attempt failed. Please try again", null)
            }
        }.addOnFailureListener {
            status.value = Resource.error("Attempt failed. Please try again later", null)
        }
    }
}