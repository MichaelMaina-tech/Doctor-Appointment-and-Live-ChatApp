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

class ManagePatientsActivity : AppCompatActivity() {

    private lateinit var recyclerViewPatients: RecyclerView
    private lateinit var buttonAddPatient: Button
    private lateinit var patientsAdapter: PatientsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var patientsListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_patients)

        recyclerViewPatients = findViewById(R.id.recyclerViewPatients)
        buttonAddPatient = findViewById(R.id.buttonAddPatient)

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        loadPatients()

        buttonAddPatient.setOnClickListener {
            // Open AddPatientActivity to add a new patient
            startActivity(Intent(this, AddPatientsActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerViewPatients.layoutManager = LinearLayoutManager(this)
        patientsAdapter = PatientsAdapter(mutableListOf()) { patient ->
            // Handle patient item click (e.g., show options to edit or delete)
            showPatientOptions(patient)
        }
        recyclerViewPatients.adapter = patientsAdapter
    }

    private fun loadPatients() {
        patientsListener = firestore.collection("patients")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error loading patients: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val patients = snapshot.toObjects(Patient::class.java)
                    patientsAdapter.updatePatients(patients)
                }
            }
    }

    private fun showPatientOptions(patient: Patient) {
        // Show options to edit or delete the patient
        // You could use a dialog or a context menu here
    }

    override fun onDestroy() {
        super.onDestroy()
        patientsListener.remove()
    }
}
