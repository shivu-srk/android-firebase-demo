package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class DashboardActivity : AppCompatActivity() {
    private fun requests() {
        val send = Intent(this, ViewRequestsActivity::class.java)
        startActivity(send)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val viewRequest = findViewById<Button>(R.id.menu2)

        viewRequest.setOnClickListener {
            requests()
        }
    }
}