package com.example.doctorappointmentapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddDoctorActivity : AppCompatActivity() {

    private lateinit var editTextDoctorName: EditText
    private lateinit var editTextDoctorEmail: EditText
    private lateinit var editTextDoctorPhone: EditText
    private lateinit var editTextDoctorSpecialization: EditText
    private lateinit var editTextDoctorPassword: EditText
    private lateinit var buttonSaveDoctor: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)

        editTextDoctorName = findViewById(R.id.editTextDoctorName)
        editTextDoctorEmail = findViewById(R.id.editTextDoctorEmail)
        editTextDoctorPhone = findViewById(R.id.editTextDoctorPhone)
        editTextDoctorSpecialization = findViewById(R.id.editTextDoctorSpecialization)
        editTextDoctorPassword = findViewById(R.id.editTextDoctorPassword)
        buttonSaveDoctor = findViewById(R.id.buttonSaveDoctor)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        buttonSaveDoctor.setOnClickListener {
            saveDoctor()
        }
    }

    private fun saveDoctor() {
        val name = editTextDoctorName.text.toString().trim()
        val email = editTextDoctorEmail.text.toString().trim()
        val phone = editTextDoctorPhone.text.toString().trim()
        val specialization = editTextDoctorSpecialization.text.toString().trim()
        val password = editTextDoctorPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || specialization.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Successfully created a user account
                    val doctor = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phoneNumber" to phone,
                        "specialization" to specialization
                    )

                    // Add doctor details to Firestore
                    firestore.collection("doctors")
                        .document(email) // Use email as document ID for easy retrieval
                        .set(doctor)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show()
                            finish() // Close activity after saving
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error adding doctor: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Failed to create a user account
                    Toast.makeText(this, "Error creating doctor account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
