package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatientsAdapter(
    private val patients: MutableList<Patient>,
    private val clickListener: (Patient) -> Unit
) : RecyclerView.Adapter<PatientsAdapter.PatientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position], clickListener)
    }

    override fun getItemCount(): Int = patients.size

    fun updatePatients(newPatients: List<Patient>) {
        patients.clear()
        patients.addAll(newPatients)
        notifyDataSetChanged()
    }

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewPatientName: TextView = itemView.findViewById(R.id.textViewPatientName)

        fun bind(patient: Patient, clickListener: (Patient) -> Unit) {
            textViewPatientName.text = patient.name
            itemView.setOnClickListener { clickListener(patient) }
        }
    }
}
