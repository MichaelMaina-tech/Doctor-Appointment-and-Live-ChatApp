package com.example.doctorappointmentapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatWithDoctorActivity : AppCompatActivity() {

    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: Button

    private lateinit var messageAdapter: ChatAdapter
    private lateinit var messageList: MutableList<ChatMessage>

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_doctor)

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendMessage = findViewById(R.id.buttonSendMessage)

        messageList = mutableListOf()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        messageAdapter = ChatAdapter(messageList, currentUserId)
        recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        recyclerViewMessages.adapter = messageAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("messages")

        loadMessages()

        buttonSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages() {
        databaseReference.child(currentUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(ChatMessage::class.java)
                    message?.let { messageList.add(it) }
                }
                messageAdapter.notifyDataSetChanged()
                recyclerViewMessages.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatWithDoctorActivity, "Failed to load messages: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage() {
        val messageText = editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val messageId = databaseReference.child(currentUserId).push().key
            if (messageId != null) {
                val message = ChatMessage(
                    messageId = messageId,
                    senderId = currentUserId,
                    receiverId = "defaultReceiverId",  // Or handle appropriately
                    messageText = messageText,
                    timestamp = System.currentTimeMillis()
                )

                databaseReference.child(currentUserId).child(messageId).setValue(message)
                editTextMessage.text.clear()
            } else {
                Toast.makeText(this, "Failed to generate message ID", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
        }
    }
}
