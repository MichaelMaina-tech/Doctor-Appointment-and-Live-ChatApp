package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        // Initialize views
        editTextUsername = findViewById(R.id.editTextAdminUsername)
        editTextPassword = findViewById(R.id.editTextAdminPassword)
        buttonLogin = findViewById(R.id.buttonAdminLoginSubmit)

        // Login Button Click Listener
        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validate username and password fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform admin login logic
            if (username == "admin" && password == "admin123") {
                // Successful login, navigate to Admin Dashboard
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish() // Close login activity
            } else {
                // Login failed, display error message
                Toast.makeText(this, "Login failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
