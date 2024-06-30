package com.example.sisadesc.ui.posts

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Post
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

data class FormState(
    val title: String = "",
    val description: String = "",
    val isSending: Boolean = false,
    val isEnableToSend: Boolean = false,
    val errorMessage: String = ""
)

class PostsViewModel : ViewModel() {
    private val _formState: MutableLiveData<FormState> = MutableLiveData<FormState>().apply {
        value = FormState()
    }
    val formState: LiveData<FormState> = _formState

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

    fun onSubmitCreateForm(onResult: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                _formState.value = _formState.value?.copy(isSending = true)

                val post = Post(
                    title = _formState.value?.title ?: "",
                    description = _formState.value?.description?.trim() ?: "",
                    date = Timestamp.now()
                )

                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .add(post.toMap())
                    .addOnSuccessListener {
                        _formState.value =
                            _formState.value?.copy(isSending = false, errorMessage = "")
                        onResult()

                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        _formState.value = _formState.value?.copy(
                            isSending = false,
                            errorMessage = e.message ?: ""
                        )

                        onError()
                    }
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "Error validating form: ${e.message ?: ""}"
                )
                _formState.value =
                    _formState.value?.copy(isSending = false, errorMessage = e.message ?: "")
            }
        }
    }

    fun onFormInputChange(title: String, description: String) {
        _formState.value = _formState.value?.copy(title = title, description = description)
        _formState.value = _formState.value?.copy(
            isEnableToSend = isValidTitle(title) && isValidDescription(description)
        )
    }

    private fun isValidTitle(title: String): Boolean {
        return title.isNotBlank() && title.length > 5
    }

    private fun isValidDescription(description: String): Boolean {
        return description.isNotBlank() && description.length > 10
    }
}