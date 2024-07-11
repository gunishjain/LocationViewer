package com.gunishjain.locationviewer.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object SharedPref {

    fun storeData(email: String, name: String, caste: String, phone: String, imageUrl: String,context: Context) {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        val editor: Editor = sharedPreferences.edit()
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putString("caste",caste)
        editor.putString("phone",phone)
        editor.putString("imageUrl",imageUrl)
        editor.apply()
    }

    fun getName(context: Context) : String {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",
            MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }

    fun getEmail(context: Context) : String {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",
            MODE_PRIVATE)
        return sharedPreferences.getString("email","")!!
    }

    fun getCaste(context: Context) : String {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",
            MODE_PRIVATE)
        return sharedPreferences.getString("caste","")!!
    }

    fun getPhone(context: Context) : String {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",
            MODE_PRIVATE)
        return sharedPreferences.getString("phone","")!!
    }
    fun getImage(context: Context) : String {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("users",
            MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl","")!!
    }


}