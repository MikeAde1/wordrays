package com.mike.wordrays.data.model

data class Member(
    val full_name: String = "",
    val address: String = "",
    val occupation: String = "",
    val phone_number: String = "",
    val dob: String = "",
    val user_id: String = "",
    val qualification: String = "",
    val email: String = "",
    val admin: Boolean = false
)