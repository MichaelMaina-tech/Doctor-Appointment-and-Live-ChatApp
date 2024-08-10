package com.example.doctorappointmentapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewScheduleActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_schedule)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerViewAppointments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        appointmentsAdapter = AppointmentsAdapter(emptyList())
        recyclerView.adapter = appointmentsAdapter

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load appointments
        loadAppointments()
    }

    private fun loadAppointments() {
        val userId = auth.currentUser?.uid ?: run {
            Log.w(TAG, "No user logged in")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("appointments")
            .whereEqualTo("doctorId", userId)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d(TAG, "No appointments found")
                    Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val appointments = result.documents.mapNotNull { document ->
                    document.toObject(Appointment::class.java)?.apply { id = document.id }
                }

                Log.d(TAG, "Fetched ${appointments.size} appointments")
                appointmentsAdapter.updateAppointments(appointments)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error loading appointments", exception)
                Toast.makeText(this, "Error loading appointments: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "ViewScheduleActivity"
    }
}
