package com.example.sisadesc.ui.user

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Role
import com.example.sisadesc.core.model.UserDetailed
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val _users = MutableLiveData<List<UserDetailed>>()
    val users: MutableLiveData<List<UserDetailed>> = _users

    private val _loading = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = _loading

    init {
        getUsers()
    }

    fun getUsers(){
        viewModelScope.launch {
            try {
                _loading.value = true
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val userObjects = documents.toObjects<UserDetailed>()

                            // List to save the users with their roles
                            val updatedUsers = mutableListOf<UserDetailed>()

                            // Counter to be sure that all roles's requests have been processed
                            var rolesProcessed = 0

                            // Total of users to process
                            val totalUsers = userObjects.size

                            userObjects.forEach { user ->
                                val roleRef = user.role

                                roleRef?.get()?.addOnSuccessListener { roleDoc ->
                                    if (roleDoc != null) {
                                        val roleData = roleDoc.data
                                        Log.d(ContentValues.TAG, "Role DocumentSnapshot data: $roleData")

                                        user.roleData = roleDoc.toObject<Role>()
                                    }
                                    rolesProcessed++
                                    updatedUsers.add(user)

                                    // Verify that all roles have been processed
                                    if (rolesProcessed == totalUsers) {
                                        _users.value = updatedUsers
                                    }
                                }?.addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "Role get failed with (${user.id}) ", exception)
                                    rolesProcessed++

                                    // Verify that all roles have been processed
                                    if (rolesProcessed == totalUsers) {
                                        _users.value = updatedUsers
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents (firebase): ", exception)
                        _users.value = emptyList()
                    }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "Error getting documents (try): ${e.message}")
                _users.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}