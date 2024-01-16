package com.example.phonenumberotp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var btnAddData : Button
    private lateinit var btnGetData : Button
    private lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnAddData = findViewById(R.id.btnAddData)
        btnGetData = findViewById(R.id.btnGetData)
        databaseRef = Firebase.database.getReference("users")

        val user_1 = User(
            name = "krishna",
            age = 20,
            email = "xyz@gmail.com"
        )

        val user_2 = User(
            name = "Siddharth Singh Gaur",
            age = 26,
            email = "xyz@gmail.com"
        )

        btnAddData.setOnClickListener(){
            //addDataToFirebaseDatabase()
            databaseRef.child("user_1").setValue(user_1)
            databaseRef.child("user_2").setValue(user_2)
        }

        btnGetData.setOnClickListener(){
            GetAllfirebaseData()
        }
    }

    private fun GetAllfirebaseData(){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot in snapshot.children){
                    val CurrentUser = dataSnapshot.getValue(User::class.java)
                    Log.i("CurrentUser",CurrentUser.toString())
                    Toast.makeText(this@HomeActivity, "data fetched", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "some error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addDataToFirebaseDatabase(user: User){

    }
}

data class User(
    var name : String = "",
    var age : Int = 0,
    var email : String = ""
    )