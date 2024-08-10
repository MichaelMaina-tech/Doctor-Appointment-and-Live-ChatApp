package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button

    companion object {
        private const val TAG = "PatientRegisterActivity"
        private const val EMAIL_REQUIRED = "Email is required"
        private const val VALID_EMAIL_REQUIRED = "Please enter a valid email"
        private const val PASSWORD_REQUIRED = "Password is required"
        private const val PASSWORD_LENGTH_ERROR = "Password should be at least 6 characters"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_register)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (!validateInputs(email, password)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserData(email)
                } else {
                    showToast("Registration failed: ${task.exception?.message}")
                    Log.e(TAG, "Registration failed", task.exception)
                }
            }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail.error = EMAIL_REQUIRED
            editTextEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = VALID_EMAIL_REQUIRED
            editTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = PASSWORD_REQUIRED
            editTextPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            editTextPassword.error = PASSWORD_LENGTH_ERROR
            editTextPassword.requestFocus()
            return false
        }

        return true
    }

    private fun saveUserData(email: String) {
        val user = auth.currentUser
        user?.let {
            val userData = hashMapOf(
                "email" to email,
                "userType" to "patient"
            )
            firestore.collection("users").document(it.uid)
                .set(userData)
                .addOnSuccessListener {
                    showToast("Registration successful")
                    startActivity(Intent(this, PatientDashboardActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    showToast("Failed to save user data: ${e.message}")
                    Log.e(TAG, "Failed to save user data", e)
                }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
