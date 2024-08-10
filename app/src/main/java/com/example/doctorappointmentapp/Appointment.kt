package com.example.doctorappointmentapp

data class Appointment(
    var id: String,
    val patientId: String,
    val doctor: String,
    val specialization: String,
    val date: String,
    val timeSlot: String,
    val chatId: String
)

