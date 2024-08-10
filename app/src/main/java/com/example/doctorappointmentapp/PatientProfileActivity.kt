package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PatientProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextGender: EditText
    private lateinit var buttonSaveProfile: Button
    private lateinit var buttonEditProfile: Button

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)

        // Initialize views
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextAge = findViewById(R.id.editTextAge)
        editTextGender = findViewById(R.id.editTextGender)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        buttonEditProfile = findViewById(R.id.buttonEditProfile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Load the patient's current profile
        loadCurrentProfile()

        // Set button click listeners
        buttonEditProfile.setOnClickListener {
            enableEditMode(true)
        }

        buttonSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadCurrentProfile() {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            val patientRef = database.child("patients").child(currentUserId)
            patientRef.get().addOnSuccessListener { dataSnapshot ->
                val patient = dataSnapshot.getValue(Patient::class.java)
                if (patient != null) {
                    // Load current profile data into EditText fields
                    editTextName.setText(patient.name)
                    editTextEmail.setText(patient.email)
                    editTextPhoneNumber.setText(patient.phoneNumber)
                    editTextAge.setText(patient.age.toString())
                    editTextGender.setText(patient.gender)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveProfile() {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            val patientData = Patient(
                name = editTextName.text.toString(),
                email = editTextEmail.text.toString(),
                phoneNumber = editTextPhoneNumber.text.toString(),
                age = editTextAge.text.toString().toIntOrNull() ?: 0,
                gender = editTextGender.text.toString()
            )

            database.child("patients").child(currentUserId).setValue(patientData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    enableEditMode(false) // Disable edit mode after saving
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun enableEditMode(enable: Boolean) {
        // Enable or disable EditText fields and save button based on the parameter
        editTextName.isEnabled = enable
        editTextEmail.isEnabled = enable
        editTextPhoneNumber.isEnabled = enable
        editTextAge.isEnabled = enable
        editTextGender.isEnabled = enable
        buttonSaveProfile.isEnabled = enable
    }
}
