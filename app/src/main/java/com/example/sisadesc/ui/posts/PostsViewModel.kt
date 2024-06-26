package com.example.sisadesc.ui.posts

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

class PostsViewModel() : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: MutableLiveData<List<Post>> = _posts

    private val _loading = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = _loading

    fun loadData() {
        viewModelScope.launch {
            try {
                _loading.value = true

                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            val postsList = mutableListOf<Post>()

                            documents.forEach {
                                val post = it.toObject<Post>()
                                post.id = it.id

                                postsList.add(post)
                            }

                            println(postsList)
                            _posts.value = postsList
                        } else {
                            _posts.value = emptyList()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                    .addOnCompleteListener {
                        _loading.value = false
                    }
            } catch (e: Exception) {
                Log.d(TAG, e.message ?: "")
                _loading.value = false
            }
        }
    }
}