package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ManageDoctorsActivity : AppCompatActivity() {

    private lateinit var recyclerViewDoctors: RecyclerView
    private lateinit var buttonAddDoctor: Button
    private lateinit var doctorsAdapter: DoctorsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var doctorsListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_doctors)

        recyclerViewDoctors = findViewById(R.id.recyclerViewDoctors)
        buttonAddDoctor = findViewById(R.id.buttonAddDoctor)

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        loadDoctors()

        buttonAddDoctor.setOnClickListener {
            // Open AddDoctorActivity to add a new doctor
            startActivity(Intent(this, AddDoctorActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerViewDoctors.layoutManager = LinearLayoutManager(this)
        doctorsAdapter = DoctorsAdapter(mutableListOf()) { doctor ->
            // Handle doctor item click (e.g., show options to edit or delete)
            showDoctorOptions(doctor)
        }
        recyclerViewDoctors.adapter = doctorsAdapter
    }

    private fun loadDoctors() {
        doctorsListener = firestore.collection("doctors")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error loading doctors: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val doctors = snapshot.toObjects(Doctor::class.java)
                    doctorsAdapter.updateDoctors(doctors)
                }
            }
    }

    private fun showDoctorOptions(doctor: Doctor) {
        // Show options to edit or delete the doctor
        // You could use a dialog or a context menu here
    }

    override fun onDestroy() {
        super.onDestroy()
        doctorsListener.remove()
    }
}
