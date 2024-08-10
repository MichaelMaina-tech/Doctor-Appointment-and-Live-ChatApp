package com.example.doctorappointmentapp

data class Doctor(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val specialization: String = "",
    val password: String = "" // New field for password
)
