package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Set up the navigation menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> navigateTo(DoctorProfileActivity::class.java)
                R.id.nav_view_schedule -> navigateTo(ViewScheduleActivity::class.java)
                R.id.nav_manage_appointments -> navigateTo(ManageAppointmentsActivity::class.java)
                R.id.nav_chat_with_patients -> navigateTo(ChatWithPatientsActivity::class.java)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Optionally, open the drawer at the start
        drawerLayout.openDrawer(GravityCompat.START)
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
}
