package com.example.actividadinterfacesfirebase

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.txtLastName
import kotlinx.android.synthetic.main.activity_main.txtName


class MainActivity : AppCompatActivity() {
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid=intent.getStringExtra("uid").toString()

        database= FirebaseDatabase.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference.child("User").child(uid)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue(User::class.java)
                txtName.setText(user?.firstName)
                txtLastName.setText(user?.lastName)
                txtPhone.setText(user?.phone)
                txtAge.setText(user?.age)
                Log.e("onDataChange", "onDataChange:" + dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
        dbReference.addValueEventListener(eventListener)
    }

    fun ok(view: View) {
        val name: String=txtName.text.toString()
        val lastName: String=txtLastName.text.toString()
        val age: String=txtAge.text.toString()
        val phone: String=txtPhone.text.toString()

        val user = User(name, lastName,age, phone)
        val userBD= dbReference.setValue(user)
    }
}