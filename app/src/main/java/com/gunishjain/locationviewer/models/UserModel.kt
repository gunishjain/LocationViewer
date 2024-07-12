package com.gunishjain.locationviewer.models

data class UserModel(val email: String = "",
                     val password: String = "",
                     val name: String = "",
                     val caste: String= "",
                     val phone: String= "",
                     val userType: String = "sharer",
                     val imageUrl: String = "",
                     val uid: String="")
