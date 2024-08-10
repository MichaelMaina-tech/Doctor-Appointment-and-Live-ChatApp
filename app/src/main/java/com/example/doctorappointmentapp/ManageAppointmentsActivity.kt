package com.example.doctorappointmentapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageAppointmentsActivity : ComponentActivity() {

    private lateinit var recyclerViewAppointments: RecyclerView
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonUpdateAvailability: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_appointments)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments)
        buttonUpdateAvailability = findViewById(R.id.buttonUpdateAvailability)

        // Set up RecyclerView
        recyclerViewAppointments.layoutManager = LinearLayoutManager(this)
        appointmentsAdapter = AppointmentsAdapter(emptyList())
        recyclerViewAppointments.adapter = appointmentsAdapter

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button listener
        buttonUpdateAvailability.setOnClickListener {
            updateAvailability()
        }

        // Fetch appointments
        fetchAppointments()
    }

    private fun fetchAppointments() {
        val currentDoctorEmail = auth.currentUser?.email
        if (currentDoctorEmail != null) {
            firestore.collection("appointments")
                .whereEqualTo("doctor", currentDoctorEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(this, "No appointments found.", Toast.LENGTH_SHORT).show()
                    } else {
                        val appointments = documents.mapNotNull { document ->
                            document.toObject(Appointment::class.java)
                        }
                        appointmentsAdapter.updateAppointments(appointments)
                        Log.d(TAG, "Appointments fetched successfully")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error fetching appointments: ${exception.message}", exception)
                    Toast.makeText(this, "Error fetching appointments: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No doctor is logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAvailability() {
        val currentDoctorEmail = auth.currentUser?.email
        if (currentDoctorEmail != null) {
            // Prompt doctor to enter their availability (for simplicity, using a Toast here)
            Toast.makeText(this, "Update availability for $currentDoctorEmail", Toast.LENGTH_SHORT).show()
            // You can replace the above with an actual dialog or input form to get the availability status from the doctor
        } else {
            Toast.makeText(this, "No doctor is logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "ManageAppointmentsActivity"
    }
}
