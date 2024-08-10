package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class PatientDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_dashboard)

        // Initialize UI components
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Set up the navigation menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> navigateTo(PatientProfileActivity::class.java)
                R.id.nav_symptom_checker -> navigateTo(SymptomCheckerActivity::class.java)
                R.id.nav_book_appointment -> navigateTo(BookAppointmentActivity::class.java)
                R.id.nav_chat_with_doctor -> navigateTo(ChatWithDoctorActivity::class.java)
                else -> false
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onProfileClick(view: View) {
        navigateTo(PatientProfileActivity::class.java)
    }

    fun onSymptomCheckerClick(view: View) {
        navigateTo(SymptomCheckerActivity::class.java)
    }

    fun onBookAppointmentClick(view: View) {
        navigateTo(BookAppointmentActivity::class.java)
    }

    fun onChatWithDoctorClick(view: View) {
        navigateTo(ChatWithDoctorActivity::class.java)
    }
}
