package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorsAdapter(
    private val doctors: MutableList<Doctor>,
    private val clickListener: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctors[position], clickListener)
    }

    override fun getItemCount(): Int = doctors.size

    fun updateDoctors(newDoctors: List<Doctor>) {
        doctors.clear()
        doctors.addAll(newDoctors)
        notifyDataSetChanged()
    }

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDoctorName: TextView = itemView.findViewById(R.id.textViewDoctorName)

        fun bind(doctor: Doctor, clickListener: (Doctor) -> Unit) {
            textViewDoctorName.text = doctor.name
            itemView.setOnClickListener { clickListener(doctor) }
        }
    }
}
