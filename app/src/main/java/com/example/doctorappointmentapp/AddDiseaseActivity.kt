package com.example.doctorappointmentapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddDiseaseActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var editTextDiseaseName: EditText
    private lateinit var editTextDiseaseDescription: EditText
    private lateinit var buttonAddDisease: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_disease)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Get references to the views
        editTextDiseaseName = findViewById(R.id.editTextDiseaseName)
        editTextDiseaseDescription = findViewById(R.id.editTextDiseaseDescription)
        buttonAddDisease = findViewById(R.id.buttonAddDisease)

        buttonAddDisease.setOnClickListener {
            addDisease()
        }
    }

    private fun addDisease() {
        val diseaseName = editTextDiseaseName.text.toString().trim()
        val diseaseDescription = editTextDiseaseDescription.text.toString().trim()

        if (diseaseName.isNotEmpty() && diseaseDescription.isNotEmpty()) {
            val disease = hashMapOf(
                "name" to diseaseName,
                "description" to diseaseDescription
            )

            db.collection("diseases")
                .add(disease)
                .addOnSuccessListener {
                    // Handle success
                    showToast("Disease added successfully")
                    finish() // Close activity
                }
                .addOnFailureListener {
                    // Handle error
                    showToast("Failed to add disease")
                }
        } else {
            showToast("Please enter all fields")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
