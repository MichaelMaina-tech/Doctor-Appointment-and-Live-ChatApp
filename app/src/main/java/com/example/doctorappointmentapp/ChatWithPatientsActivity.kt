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

class ChatWithPatientsActivity : AppCompatActivity() {

    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_patients)

        initializeViews()
        initializeFirebase()
        setupRecyclerView()

        val patientId = intent.getStringExtra("PATIENT_ID").orEmpty()
        val chatId = generateChatId(FirebaseAuth.getInstance().currentUser?.uid.orEmpty(), patientId)

        if (chatId.isEmpty()) {
            showToast("Chat ID is missing")
            finish()
            return
        }

        loadChatMessages(chatId)

        buttonSend.setOnClickListener {
            sendMessage(chatId, patientId)
        }
    }

    private fun initializeViews() {
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)
    }

    private fun initializeFirebase() {
        database = FirebaseDatabase.getInstance().reference
    }

    private fun setupRecyclerView() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        chatAdapter = ChatAdapter(messageList, currentUserId)
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        recyclerViewChat.adapter = chatAdapter
    }

    private fun loadChatMessages(chatId: String) {
        val chatRef = database.child("chats").child(chatId).child("messages")

        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                dataSnapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
                    .let { messageList.addAll(it) }
                chatAdapter.notifyDataSetChanged()
                recyclerViewChat.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Failed to load messages: ${databaseError.message}")
            }
        })
    }

    private fun sendMessage(chatId: String, receiverId: String) {
        val messageText = editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val messageId = database.child("chats").child(chatId).child("messages").push().key
            if (messageId != null) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

                val message = ChatMessage(
                    messageId = messageId,
                    senderId = currentUserId,
                    receiverId = receiverId,
                    messageText = messageText,
                    timestamp = System.currentTimeMillis()
                )

                database.child("chats").child(chatId).child("messages").child(messageId).setValue(message)
                editTextMessage.text.clear()
            } else {
                showToast("Failed to generate message ID")
            }
        } else {
            showToast("Please enter a message")
        }
    }

    private fun generateChatId(userId: String, patientId: String): String {
        return if (userId > patientId) "$userId-$patientId" else "$patientId-$userId"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
