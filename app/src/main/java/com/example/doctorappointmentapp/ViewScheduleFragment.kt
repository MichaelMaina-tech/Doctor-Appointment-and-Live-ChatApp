package com.example.doctorappointmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ViewScheduleFragment : Fragment() {

    private lateinit var recyclerViewSchedule: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_schedule, container, false)

        recyclerViewSchedule = view.findViewById(R.id.recyclerViewSchedule)
        recyclerViewSchedule.layoutManager = LinearLayoutManager(requireContext())

        scheduleAdapter = ScheduleAdapter(listOf())
        recyclerViewSchedule.adapter = scheduleAdapter

        firestore = FirebaseFirestore.getInstance()
        loadSchedule()

        return view
    }

    private fun loadSchedule() {
        firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                val schedules = result.map { it.toObject<Schedule>() }
                scheduleAdapter.updateSchedule(schedules)
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
            }
    }
}
