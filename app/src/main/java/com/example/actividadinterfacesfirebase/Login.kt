package com.example.actividadinterfacesfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar= ProgressBar(this)
        auth= FirebaseAuth.getInstance()
    }

    fun Login(view: View) {
        loginUser()
    }
    fun forgotPassword(view: View) {
        startActivity(Intent(this, ForgotPassword::class.java))
    }
    fun register(view: View) {
        startActivity(Intent(this, Register::class.java))
    }
    private fun loginUser(){
        val user:String=txtUser.text.toString()
        val password:String=txtPassword.text.toString()

        if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    progressBar.visibility=View.VISIBLE
                    action()
                }else{
                    Toast.makeText(this,"Error en la autenticación", Toast.LENGTH_LONG).show()

                }
            }

        }

    }
    private fun action(){
        var intent= Intent(this, MainActivity::class.java)
        intent.putExtra("uid",auth.currentUser?.uid)
        startActivity(intent)
    }
}