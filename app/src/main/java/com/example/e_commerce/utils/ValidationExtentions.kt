package com.example.e_commerce.utils

fun String.isEmailValid(): Boolean  {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}