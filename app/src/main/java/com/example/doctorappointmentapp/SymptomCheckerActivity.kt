package com.example.doctorappointmentapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SymptomCheckerActivity : AppCompatActivity() {

    private lateinit var editTextSymptoms: EditText
    private lateinit var buttonCheckSymptoms: Button
    private lateinit var textViewResult: TextView
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_checker)

        // Initialize views
        editTextSymptoms = findViewById(R.id.editTextSymptoms)
        buttonCheckSymptoms = findViewById(R.id.buttonCheckSymptoms)
        textViewResult = findViewById(R.id.textViewResult)

        // Set button click listener
        buttonCheckSymptoms.setOnClickListener {
            val symptomsInput = editTextSymptoms.text.toString().trim()

            if (symptomsInput.isEmpty()) {
                Toast.makeText(this, "Please enter symptoms.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Fetch diseases from Firestore and check symptoms
            checkSymptomsInDatabase(symptomsInput)
        }
    }

    private fun checkSymptomsInDatabase(symptomsInput: String) {
        val symptomsList = symptomsInput.split(",").map { it.trim().lowercase() }

        firestore.collection("diseases")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    textViewResult.text = "No diseases found in the database."
                    return@addOnSuccessListener
                }

                var diseaseFound = false
                val diseaseScores = mutableMapOf<String, Int>()

                for (document in result) {
                    val docSymptoms = document.get("symptoms") as? List<String> ?: emptyList()
                    val diseaseName = document.getString("name") ?: "Unknown Disease"

                    // Count matching symptoms
                    val matchCount = docSymptoms.count { symptom -> symptomsList.contains(symptom.lowercase()) }
                    if (matchCount > 0) {
                        diseaseScores[diseaseName] = matchCount
                        diseaseFound = true
                    }
                }

                // Determine the most likely disease
                if (diseaseFound) {
                    val mostLikelyDisease = diseaseScores.maxByOrNull { it.value }
                    val mostLikelyDiseaseName = mostLikelyDisease?.key ?: "Unknown Disease"
                    val matchScore = mostLikelyDisease?.value ?: 0

                    textViewResult.text = "Possible matching diseases: ${diseaseScores.keys.joinToString(", ")}\n" +
                            "Most likely disease: $mostLikelyDiseaseName with a matching score of $matchScore"
                } else {
                    textViewResult.text = "No matching disease found. Please consult a doctor."
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch data from Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
