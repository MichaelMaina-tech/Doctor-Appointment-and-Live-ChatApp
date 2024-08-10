package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiseasesAdapter(
    private val diseases: MutableList<Disease>,
    private val clickListener: (Disease) -> Unit
) : RecyclerView.Adapter<DiseasesAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_disease, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseases[position], clickListener)
    }

    override fun getItemCount(): Int = diseases.size

    fun updateDiseases(newDiseases: List<Disease>) {
        diseases.clear()
        diseases.addAll(newDiseases)
        notifyDataSetChanged()
    }

    class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDiseaseName: TextView = itemView.findViewById(R.id.textViewDiseaseName)

        fun bind(disease: Disease, clickListener: (Disease) -> Unit) {
            textViewDiseaseName.text = disease.name
            itemView.setOnClickListener { clickListener(disease) }
        }
    }
}
