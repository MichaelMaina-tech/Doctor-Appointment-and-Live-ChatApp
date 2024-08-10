package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorLoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_login)

        auth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.editTextDoctorEmail)
        editTextPassword = findViewById(R.id.editTextDoctorPassword)
        buttonLogin = findViewById(R.id.buttonDoctorLoginSubmit)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Verify if user is a doctor
                        checkIfDoctor(email) { isDoctor ->
                            if (isDoctor) {
                                // Login successful and user is a doctor
                                val intent = Intent(this, DoctorDashboardActivity::class.java)
                                intent.putExtra("doctorEmail", email) // Pass doctor email to dashboard
                                startActivity(intent)
                                finish()
                            } else {
                                // User is not a doctor
                                auth.signOut()
                                Toast.makeText(this, "Login failed. You are not authorized to access this dashboard.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Login failed, show error message
                        Toast.makeText(this, "Login failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkIfDoctor(email: String, callback: (Boolean) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("doctors").document(email).get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error checking user role: ${it.message}", Toast.LENGTH_SHORT).show()
                callback(false)
            }
    }
}
