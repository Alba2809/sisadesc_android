package com.example.sisadesc.ui.posts

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.launch

class PostsViewModel() : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: MutableLiveData<List<Post>> = _posts

    private val _loading = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = _loading

    fun getPosts() {
        viewModelScope.launch {
            try {
                _loading.value = true
                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            val postObjects = documents.toObjects<Post>()
                            println(postObjects)
                            _posts.value = postObjects
                        } else {
                            _posts.value = emptyList()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            } catch (e: Exception) {
                Log.d(TAG, e.message ?: "")
            } finally {
                _loading.value = false
            }
        }
    }
}