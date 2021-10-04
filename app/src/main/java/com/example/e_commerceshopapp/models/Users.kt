package com.example.e_commerceshopapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val image : String = "",
    val mobile : Long = 0,
    val gender : String = "",
    val profileCompleted : Int = 0
) : Parcelable