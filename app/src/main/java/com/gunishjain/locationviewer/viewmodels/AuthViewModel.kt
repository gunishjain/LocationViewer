package com.gunishjain.locationviewer.viewmodels


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import com.gunishjain.locationviewer.models.UserModel
import com.gunishjain.locationviewer.utils.Constants.FIREBASE_DB_URL
import com.gunishjain.locationviewer.utils.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_DB_URL)
    private val userRef: DatabaseReference = db.getReference("users")
    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef: StorageReference = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }


    fun login(email: String, password: String,context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.value = auth.currentUser
                    getUserData(auth.currentUser!!.uid, context = context)
                } else {
                    _error.value = it.exception.toString()
                }
            }
    }

    private fun getUserData(uid: String,context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData: UserModel? = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(userData!!.email,userData.name,userData.caste,userData.phone,userData.imageUrl,context)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        caste: String,
        phone: String,
        selectedImageUri: Uri?,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.value = auth.currentUser
                    saveImage(
                        email,
                        password,
                        name,
                        caste,
                        phone,
                        selectedImageUri,
                        auth.currentUser?.uid,
                        context
                    )
                } else {
                    _error.value = "Something went wrong!"
                }
            }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        caste: String,
        phone: String,
        selectedImageUri: Uri?,
        uid: String?,
        context: Context
    ) {
        val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email, password, name, caste, phone, it.toString(), uid, context)
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        caste: String,
        phone: String,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {
        val userData = UserModel(email, password, name, caste, phone, userType = "sharer", imageUrl, uid!!)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {

                SharedPref.storeData(email, name, caste, phone, imageUrl, context)
                Log.d("VM", "real")

            }.addOnFailureListener {
                Log.d("VM", it.toString())
            }
    }

    fun logOut() {
        auth.signOut()
        _firebaseUser.value=null
    }


}