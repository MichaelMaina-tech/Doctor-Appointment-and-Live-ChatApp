package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for displaying a list of appointments
class AppointmentsAdapter(
    private var appointments: List<Appointment>
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    // Inflates the item layout and creates a ViewHolder for each appointment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    // Binds the appointment data to the ViewHolder
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    // Returns the total number of items in the data set
    override fun getItemCount(): Int = appointments.size

    // Updates the list of appointments and notifies the adapter of data changes
    fun updateAppointments(newAppointments: List<Appointment>) {
        this.appointments = newAppointments
        notifyDataSetChanged()
    }

    // ViewHolder class to hold references to each view in the item layout
    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDoctor: TextView = itemView.findViewById(R.id.textViewDoctor)
        private val textViewSpecialization: TextView = itemView.findViewById(R.id.textViewSpecialization)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewTimeSlot: TextView = itemView.findViewById(R.id.textViewTimeSlot)

        // Binds the appointment data to the views
        fun bind(appointment: Appointment) {
            textViewDoctor.text = appointment.doctor ?: "Not Provided"
            textViewSpecialization.text = appointment.specialization ?: "Not Provided"
            textViewDate.text = appointment.date ?: "Not Provided"
            textViewTimeSlot.text = appointment.timeSlot ?: "Not Provided"
        }
    }
}
