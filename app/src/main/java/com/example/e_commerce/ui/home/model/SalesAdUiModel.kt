package com.example.e_commerce.ui.home.model

import android.util.Log
import com.example.e_commerce.utils.CountdownTimer
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

data class SalesUiAdModel(
    val id: String? = null,
    val title: String? = null,
    var productId: String? = null,
    var imageUrl: String? = null,
    var categoryId: String? = null,
    val type: String? = null,
    var externalLink: String? = null,
    var endAt: Date? = null
){
    private var timer: CountdownTimer? = null
    val hours = MutableStateFlow("")
    val seconds = MutableStateFlow("")
    val minutes = MutableStateFlow("")

    fun startCountdown() {
        endAt?.let {
            timer?.stop()
            timer = CountdownTimer(it) { hours, minutes, seconds ->
                Log.d("CountdownTimer", "hours: $hours, minutes: $minutes, seconds: $seconds")
                this.hours.value = hours.toString()
                this.seconds.value = seconds.toString()
                this.minutes.value = minutes.toString()
            }
            timer?.start()
        }
    }

    fun stopCountdown() {
        timer?.stop()
    }
}

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}