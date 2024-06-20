package com.example.sisadesc.core.auth

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

class UserViewModel() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _userLoggedData = MutableLiveData<UserLogged?>()
    val userLoggedData: MutableLiveData<UserLogged?> = _userLoggedData

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userId = auth.currentUser?.uid
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .whereEqualTo("userId", userId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val res = documents.documents[0]
                            val userLogged = res.toObject<UserLogged>()

                            Log.d(TAG, "DocumentSnapshot data: ${res.data}")

                            val roleRef = userLogged?.role

                            roleRef?.get()?.addOnSuccessListener { roleDoc ->
                                if (roleDoc != null) {
                                    val roleData = roleDoc.data

                                    userLogged.roleData = roleDoc.toObject<Role>()

                                    _userLoggedData.value = userLogged
                                }
                                else {
                                    Log.d(TAG, "Role document is null")
                                    _userLoggedData.value = userLogged
                                }
                            }?.addOnFailureListener { exception ->
                                Log.d(TAG, "Role get failed with ", exception)
                                _userLoggedData.value = userLogged
                            }
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                        _userLoggedData.value = null
                    }
            } catch (e: Exception) {
                Log.d(TAG, "Error getting documents: ${e.message}")
                _userLoggedData.value = null
            } finally {
                _loading.value = false
            }
        }

    }
}