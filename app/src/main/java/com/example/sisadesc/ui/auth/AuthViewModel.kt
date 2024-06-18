package com.example.sisadesc.ui.auth

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    private val _showPassword = MutableLiveData(false)
    val showPassword: LiveData<Boolean> = _showPassword

    private val _validData = MutableLiveData(false)
    val validData: LiveData<Boolean> = _validData

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email
    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _validData.value = isValidEmail(email) && isValidPassword(password)
    }

    fun onShowPasswordChanged() {
        _showPassword.value = !_showPassword.value!!
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        next: () -> Unit
    ) =
        viewModelScope.launch {
            try {
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _error.value = false
                            Log.d("Login", "signInWithEmailAndPassword completed")
                            next()
                        } else {
                            _error.value = true
                            Log.d(
                                "Login",
                                "signInWithEmailAndPassword: ${task.exception}"
                            )
                            Toast.makeText(
                                context,
                                "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } catch (ex: Exception) {
                Log.d("Login", "signInWithEmailAndPassword: ${ex.message}")
            } finally {
                _loading.value = false
            }
        }
}