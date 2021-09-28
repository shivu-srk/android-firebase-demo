package com.example.helloworld

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewRequestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_requests)
        val requestListView: RecyclerView = findViewById(R.id.requestListView)
        val adapter = ViewRequestListAdapter()
        requestListView.adapter = adapter
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        if(user != null){
            db.collection("requests")
                .whereEqualTo("uId", user.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.documents.map {
                        it["id"].toString()
                    }
                    adapter.list = list
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    "Failed to retrieve requests".showToast(this)
                }
        } else {
            finish()
        }
    }
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}