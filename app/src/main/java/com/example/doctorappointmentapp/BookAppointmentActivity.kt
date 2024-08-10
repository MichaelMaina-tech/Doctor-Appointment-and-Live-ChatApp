package com.example.doctorappointmentapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var spinnerDoctors: Spinner
    private lateinit var buttonSelectDate: Button
    private lateinit var textViewSelectedDate: TextView
    private lateinit var spinnerTimeSlots: Spinner
    private lateinit var buttonConfirmAppointment: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var doctorList: MutableList<String>
    private lateinit var doctorMap: MutableMap<String, Pair<String, String>> // Map doctor name to (id, specialization)

    private val timeSlots = listOf(
        "9:00 AM - 10:00 AM",
        "10:00 AM - 11:00 AM",
        "11:00 AM - 12:00 PM",
        "1:00 PM - 2:00 PM",
        "2:00 PM - 3:00 PM",
        "3:00 PM - 4:00 PM",
        "4:00 PM - 5:00 PM"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        spinnerDoctors = findViewById(R.id.spinnerDoctors)
        buttonSelectDate = findViewById(R.id.buttonSelectDate)
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate)
        spinnerTimeSlots = findViewById(R.id.spinnerTimeSlots)
        buttonConfirmAppointment = findViewById(R.id.buttonConfirmAppointment)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        doctorList = mutableListOf()
        doctorMap = mutableMapOf()

        fetchDoctors()
        setupTimeSlotSpinner()

        buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        buttonConfirmAppointment.setOnClickListener {
            confirmAppointment()
        }
    }

    private fun fetchDoctors() {
        firestore.collection("doctors").get().addOnSuccessListener { result ->
            doctorList.clear()
            val newDoctorMap = mutableMapOf<String, Pair<String, String>>() // Store id and specialization
            for (document in result) {
                val doctorName = document.getString("name") ?: "Unknown"
                val specialization = document.getString("specialization") ?: "General"
                doctorList.add("$doctorName - $specialization")
                newDoctorMap[doctorName] = Pair(document.id, specialization) // Store id and specialization
            }
            doctorMap = newDoctorMap
            val adapter = ArrayAdapter(
                this@BookAppointmentActivity,
                android.R.layout.simple_spinner_dropdown_item,
                doctorList
            )
            spinnerDoctors.adapter = adapter
        }.addOnFailureListener {
            Toast.makeText(this@BookAppointmentActivity, "Failed to fetch doctors.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTimeSlotSpinner() {
        val timeSlotAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            timeSlots
        )
        spinnerTimeSlots.adapter = timeSlotAdapter
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "${selectedMonth + 1}/$selectedDay/$selectedYear"
            textViewSelectedDate.text = date
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun confirmAppointment() {
        val selectedDoctorName = spinnerDoctors.selectedItem.toString().split(" - ")[0]
        val selectedDate = textViewSelectedDate.text.toString()
        val selectedTimeSlot = spinnerTimeSlots.selectedItem.toString()

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserId = auth.currentUser?.uid
        val doctorInfo = doctorMap[selectedDoctorName] // Get the doctor info (id, specialization)

        if (currentUserId != null && doctorInfo != null) {
            val (doctorId, specialization) = doctorInfo
            val chatId = "chat_${currentUserId}_$doctorId"
            val appointmentId = firestore.collection("appointments").document().id

            val appointment = hashMapOf(
                "id" to appointmentId,
                "patientId" to currentUserId,
                "doctor" to selectedDoctorName,
                "specialization" to specialization,
                "date" to selectedDate,
                "timeSlot" to selectedTimeSlot,
                "chatId" to chatId
            )

            firestore.collection("appointments").document(appointmentId).set(appointment).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                    // Optionally, start chat activity
                    // val intent = Intent(this, ChatWithDoctorActivity::class.java)
                    // intent.putExtra("CHAT_ID", chatId)
                    // startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to book appointment. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User or doctor not found.", Toast.LENGTH_SHORT).show()
        }
    }
}
