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

class ManageDiseasesActivity : AppCompatActivity() {

    private lateinit var recyclerViewDiseases: RecyclerView
    private lateinit var buttonAddDisease: Button
    private lateinit var diseasesAdapter: DiseasesAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var diseasesListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_diseases)

        recyclerViewDiseases = findViewById(R.id.recyclerViewDiseases)
        buttonAddDisease = findViewById(R.id.buttonAddDisease)

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        loadDiseases()

        buttonAddDisease.setOnClickListener {
            // Open AddDiseaseActivity to add a new disease
            startActivity(Intent(this, AddDiseaseActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerViewDiseases.layoutManager = LinearLayoutManager(this)
        diseasesAdapter = DiseasesAdapter(mutableListOf()) { disease ->
            // Handle disease item click (e.g., show options to edit or delete)
            showDiseaseOptions(disease)
        }
        recyclerViewDiseases.adapter = diseasesAdapter
    }

    private fun loadDiseases() {
        diseasesListener = firestore.collection("diseases")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error loading diseases: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val diseases = snapshot.toObjects(Disease::class.java)
                    diseasesAdapter.updateDiseases(diseases)
                }
            }
    }

    private fun showDiseaseOptions(disease: Disease) {
        // Show options to edit or delete the disease
        // You could use a dialog or a context menu here
    }

    override fun onDestroy() {
        super.onDestroy()
        diseasesListener.remove()
    }
}
