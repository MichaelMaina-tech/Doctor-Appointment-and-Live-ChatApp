package com.example.doctorappointmentapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var editTextDoctorName: EditText
    private lateinit var editTextSpecialization: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonSaveProfile: Button
    private lateinit var buttonEditProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get references to the views
        editTextDoctorName = findViewById(R.id.editTextDoctorName)
        editTextSpecialization = findViewById(R.id.editTextSpecialization)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        buttonEditProfile = findViewById(R.id.buttonEditProfile)

        // Load existing profile data
        loadProfileData()

        // Set up click listener for the save button
        buttonSaveProfile.setOnClickListener {
            saveProfileData()
        }

        // Set up click listener for the edit button
        buttonEditProfile.setOnClickListener {
            enableEditing(true)
        }
    }

    private fun loadProfileData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("doctors").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    editTextDoctorName.setText(document.getString("name"))
                    editTextSpecialization.setText(document.getString("specialization"))
                    editTextEmail.setText(document.getString("email"))
                    editTextPhone.setText(document.getString("phone"))

                    // Disable editing initially
                    enableEditing(false)
                } else {
                    Toast.makeText(this, "No profile data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfileData() {
        val userId = auth.currentUser?.uid ?: return
        val doctorName = editTextDoctorName.text.toString().trim()
        val specialization = editTextSpecialization.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()

        if (doctorName.isNotEmpty() && specialization.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
            val profileData = hashMapOf(
                "name" to doctorName,
                "specialization" to specialization,
                "email" to email,
                "phone" to phone
            )

            db.collection("doctors").document(userId)
                .set(profileData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    enableEditing(false) // Disable editing after saving
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableEditing(enable: Boolean) {
        editTextDoctorName.isEnabled = enable
        editTextSpecialization.isEnabled = enable
        editTextEmail.isEnabled = enable
        editTextPhone.isEnabled = enable
        buttonSaveProfile.isEnabled = enable
        buttonEditProfile.isEnabled = !enable // Disable edit button while editing
    }
}
