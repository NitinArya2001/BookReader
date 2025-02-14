package com.example.bookreader.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    //    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        isCreateAccount: MutableState<Boolean>,
        home: () -> Unit
    ) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        val exception = task.exception
                        val errorMessage = exception?.localizedMessage ?: "Failed to create account"

                        // ✅ Check if the exception is for an existing email
                        if (exception is FirebaseAuthUserCollisionException) {
                            Log.e("error", "User already exists: ${exception.message}")

                            // ✅ Show a toast message to the user
                            Toast.makeText(context, "User already exists. Try logging in!", Toast.LENGTH_SHORT).show()
                            isCreateAccount.value = false
                        } else {
                            Log.e("error", "Account creation failed: $errorMessage")
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            profession = "Android Developer",
            quote = "Life is Great",
            id = null).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        context: Context, // Pass context to show Toast
        home: () -> Unit
    ) =
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("nitin", "signInWithEmailAndPassword: ${task.result}")
                        home() // Navigate to home on success
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Invalid credentials"
                        Log.e("error", "signInWithEmailAndPassword: $errorMessage")

                        // Show Toast Message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }


}