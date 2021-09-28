package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private fun details() {
        val send: Intent = Intent(this, DetailsActivity::class.java)
        startActivity(send)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phone = findViewById<EditText>(R.id.phone_no)
        val otp = findViewById<EditText>(R.id.otp)
        val button = findViewById<Button>(R.id.login)
        var isLogin = false

        val db = Firebase.firestore

        if(FirebaseAuth.getInstance().currentUser != null){
            details()
        }

        println(FirebaseAuth.getInstance().currentUser)

        otp.visibility = View.GONE
        button.text = "SEND OTP"

        var storedVerificationId = ""
        var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
        button.setOnClickListener{
            if(isLogin){
                val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp.text.toString())
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = task.result?.user
                            val userMap = hashMapOf(
                                "uId" to user!!.uid,
                                "phno" to phone.text.toString()
                            )
                            db.collection("users")
                                .document(user.uid)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    if(!snapshot.exists()) {
                                        db.collection("users")
                                            .document(user.uid)
                                            .set(userMap)
                                            .addOnSuccessListener {
                                                details()
                                            }
                                            .addOnFailureListener {
                                                FirebaseAuth.getInstance().signOut()
                                            }
                                    } else {
                                            details()

                                    }

                                }
                                .addOnFailureListener {
                                    it.printStackTrace()
                                }
                        } else {
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            // Update UI
                        }
                    }
            } else {
                val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                    .setPhoneNumber("+91" + phone.text.toString())       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                            // next route
                            details()
                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            p0.printStackTrace()
                        }

                        override fun onCodeSent(
                            p0: String,
                            p1: PhoneAuthProvider.ForceResendingToken
                        ) {
                            storedVerificationId = p0
                            resendingToken = p1
                            isLogin = true
                            phone.isEnabled = false
                            otp.visibility = View.VISIBLE
                            button.text = "LOGIN"
                        }

                    })          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }
}

