package com.example.actividadinterfacesfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth= FirebaseAuth.getInstance()
    }

    fun send(view: View) {
        val email= txtEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    progressBar2.visibility=View.VISIBLE
                    startActivity(Intent(this,Login::class.java))
                }else{
                    Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}