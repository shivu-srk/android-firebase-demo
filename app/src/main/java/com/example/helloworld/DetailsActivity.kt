package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailsActivity : AppCompatActivity() {
    private fun dashboard() {
        val send= Intent(this, DashboardActivity::class.java)
        startActivity(send)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val nameText = findViewById<EditText>(R.id.name)
        val addressText = findViewById<EditText>(R.id.addr)
        val aadhaarText = findViewById<EditText>(R.id.aadhar)
        val button = findViewById<Button>(R.id.button)

        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        if(user != null){
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot["name"]
                    val addr = snapshot["addr"]
                    val aadhaar = snapshot["aadhaar"]
                    if(listOf(name, addr, aadhaar).all { it != null }) {
                        dashboard()
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    dashboard()
                }
        } else{
            finish()
        }

        button.setOnClickListener {
            val details = hashMapOf(
                "name" to nameText.text.toString(),
                "address" to addressText.text.toString(),
                "aadhaar" to aadhaarText.text.toString()
            ).toMap()
            db.collection("users")
                .document(user!!.uid)
                .update(details)
                .addOnSuccessListener {
                    dashboard()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }
}