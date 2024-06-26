package com.example.sisadesc.ui.user

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Role
import com.example.sisadesc.core.model.UserDetailed
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersViewModel : ViewModel() {
    private val _users = MutableLiveData<List<UserDetailed>?>()
    val users: MutableLiveData<List<UserDetailed>?> = _users

    private val _loading = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = _loading

    fun getUsers() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val usersResult = FirebaseFirestore.getInstance()
                    .collection("users")
                    .get()
                    .await() // Suspend function to await the result

                if (usersResult != null) {
                    val userObjects = usersResult.toObjects<UserDetailed>()

                    // List to save deferred tasks
                    val deferredTasks = userObjects.map { user ->
                        async {
                            val roleRef = user.role
                            val roleDoc = roleRef?.get()?.await() // Await the result of the role fetch
                            if (roleDoc != null) {
                                user.roleData = roleDoc.toObject<Role>()
                            }
                            user
                        }
                    }

                    // Await all tasks to complete
                    val updatedUsers = deferredTasks.awaitAll()
                    _users.value = updatedUsers
                } else {
                    _users.value = emptyList()
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "Error getting documents: ${e.message}")
                _users.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}