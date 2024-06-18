package com.example.sisadesc.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Role
import com.example.sisadesc.core.model.UserLogged
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _userData = MutableLiveData<UserLogged>()
    val userData: LiveData<UserLogged> = _userData

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .whereEqualTo("userId", userId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val res = documents.documents[0]
                            val user = res.toObject<UserLogged>()

                            Log.d(TAG, "DocumentSnapshot data: ${res.data}")

                            val roleRef = user?.role

                            roleRef?.get()?.addOnSuccessListener { roleDoc ->
                                if (roleDoc != null) {
                                    val roleData = roleDoc.data
                                    Log.d(TAG, "Role DocumentSnapshot data: $roleData")

                                    user.roleData = roleDoc.toObject<Role>()

                                    _userData.value = user
                                }
                            }?.addOnFailureListener { exception ->
                                Log.d(TAG, "Role get failed with ", exception)
                                _userData.value = null
                            }
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                        _userData.value = null
                    }
            } catch (e: Exception) {
                Log.d(TAG, "Error getting documents: ${e.message}")
                _userData.value = null
            }
        }

    }
}