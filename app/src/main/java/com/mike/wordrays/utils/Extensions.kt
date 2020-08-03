package com.mike.wordrays.utils

fun String?.isValidEmail(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
