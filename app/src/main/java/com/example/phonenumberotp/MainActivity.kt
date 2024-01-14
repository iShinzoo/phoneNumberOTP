package com.example.phonenumberotp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var edtPhoneNumber : EditText
    private lateinit var edtEnterOTP : EditText
    private lateinit var btnSendOTP : Button
    private lateinit var btnVerifyOTP : Button
    private lateinit var mAuth : FirebaseAuth
    var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = Firebase.auth
        edtPhoneNumber = findViewById(R.id.PhoneNumber)
        edtEnterOTP = findViewById(R.id.EnterOtp)
        btnSendOTP = findViewById(R.id.btn_SendOTP)
        btnVerifyOTP = findViewById(R.id.btn_VerifyOTP)

        btnSendOTP.setOnClickListener{
            var number = "+91${edtPhoneNumber.text}"
            sendVerificationCode(number)
        }

        btnVerifyOTP.setOnClickListener{
            val otp = edtEnterOTP.text.toString()
            verifyCode(otp)
        }
    }

    private fun verifyCode(code : String){
        val credential = PhoneAuthProvider.getCredential(verificationId,code)
        signInWithCredentials(credential)

    }
    private fun signInWithCredentials(phoneAuthCredential: PhoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this,HomeActivity::class.java)
                    )
                } else{
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationCode(number : String){
        Log.d("Verification", "Sending code to: $number")
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setTimeout(120L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    var mCallback : OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.d("Verification", "Verification completed")
            Toast.makeText(this@MainActivity, "verification pass", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.e("Verification", "Verification failed", p0)
            Toast.makeText(this@MainActivity, "verification fail", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationId = p0
            Log.d("Verification", "Code sent successfully")
        }
    }
}