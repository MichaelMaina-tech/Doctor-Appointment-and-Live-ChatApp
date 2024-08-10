package com.example.doctorappointmentapp

data class ChatMessage(val messageId: String = "",
                       val senderId: String = "",
                       val receiverId: String = "",
                       val messageText: String = "",
                       val timestamp: Long = 0L
)
