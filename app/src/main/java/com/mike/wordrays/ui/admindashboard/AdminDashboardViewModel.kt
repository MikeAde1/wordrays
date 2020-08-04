package com.mike.wordrays.ui.admindashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mike.wordrays.data.Resource
import com.mike.wordrays.data.model.Member

class AdminDashboardViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val databaseRef = database.getReference("users")
    val name = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val occupation = MutableLiveData<String>()
    val phone_number = MutableLiveData<String>()
    val dob = MutableLiveData<String>()
    val qualification = MutableLiveData<String>()
    val chosenMember = MutableLiveData<Member>()
    val list = MutableLiveData<Resource<List<Member>>>()
    val values = arrayListOf<Member>()
    val address = MutableLiveData<String>()
    val email = MutableLiveData<String>()


    fun getDetails() {
        //query firebase database
        databaseRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (values.size > 0) { values.clear()}
                    for (dataSnapshot1 in dataSnapshot.children) {
                        if (dataSnapshot1.child("admin").value == false) {
                            Log.d("datas", dataSnapshot1.toString())
                            val data = dataSnapshot1.getValue(Member::class.java)
                            if (data != null) {
                                values.add(data)
                            }
                        }
                    }
                    list.value = Resource.success(values, null)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("database", databaseError.message)
                    list.value = Resource.error("Something just happened", null)
                }
            })
    }

    fun setMember(member: Member) {
        name.value = member.full_name
        occupation.value = member.occupation
        phone_number.value = member.phone_number
        dob.value = member.dob
        qualification.value = member.qualification
        address.value = member.address
        email.value = member.email
    }
}