package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonPatient: Button = findViewById(R.id.buttonPatient)
        val buttonDoctor: Button = findViewById(R.id.buttonDoctor)
        val buttonAdmin: Button = findViewById(R.id.buttonAdmin)

        buttonPatient.setOnClickListener {
            startActivity(Intent(this, PatientLoginActivity::class.java))
        }

        buttonDoctor.setOnClickListener {
            startActivity(Intent(this, DoctorLoginActivity::class.java))
        }

        buttonAdmin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }
}
