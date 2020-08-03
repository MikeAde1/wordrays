package com.mike.wordrays.ui.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mike.wordrays.data.Resource
import java.lang.reflect.Member

class FillFormViewModel: ViewModel() {
    var firebaseAuth = FirebaseAuth.getInstance()
    // Write a message to the database
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("users")
    val firstName = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val dateOfBirth = MutableLiveData<String>()
    val occupation = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val errorEntry = MutableLiveData<String>()
    val qualification = MutableLiveData<String>()
    val status = MutableLiveData<Resource<String>>()

    fun sendProfile() {
        if (validateFields()) {
            status.value = Resource.loading()
            myRef.child(firebaseAuth.currentUser?.uid!!).setValue(com.mike.wordrays.data.model.Member(
                firstName.value.toString(),
                address.value.toString(),
                occupation.value.toString(),
                phoneNumber.value.toString(),
                dateOfBirth.value.toString(),
                firebaseAuth.currentUser!!.uid,
                qualification.value.toString(),
                firebaseAuth.currentUser?.email!!,
                false
            )).addOnCompleteListener {
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

    private fun validateFields(): Boolean {
        if (firstName.value.isNullOrBlank()) {
            firstName.value = "First name cannot be blank"
            return false
        }

        if (address.value.isNullOrBlank()) {
            errorEntry.value = "Address cannot be blank"
            return false
        }
        if (dateOfBirth.value.isNullOrBlank()) {
            errorEntry.value = "Please enter date of birth"
            return false
        }
        if (occupation.value.isNullOrBlank()) {
            errorEntry.value = "Please enter occupation"
            return false
        }
        if (phoneNumber.value.isNullOrBlank()) {
            errorEntry.value = "Phone number cannot be blank"
            return false
        }
        if (qualification.value.isNullOrBlank()) {
            errorEntry.value = "Please fill your qualification"
            return false
        }
        return true
    }
}