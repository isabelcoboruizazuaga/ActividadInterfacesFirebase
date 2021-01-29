package com.example.actividadinterfacesfirebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class Register : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    companion object {
        private const val RC_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        progressBar= ProgressBar(this)
        database= FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference=database.reference.child("User")
    }

    fun Register(view: View) {
        createNewAccount()
    }

    private fun createNewAccount(){
        val name: String=txtName.text.toString()
        val lastName: String=txtLastName.text.toString()
        val email: String=txtEmail.text.toString()
        val password: String=txtPassword.text.toString()

        if(!TextUtils.isEmpty((name))&& !TextUtils.isEmpty(lastName)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
            progressBar.visibility= View.VISIBLE

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    val user:FirebaseUser?=auth.currentUser
                    val uid= user?.uid!!
                    verifyEmail(user)

                    val user1 = User(name, lastName,"0","0")
                    val userBD= dbReference.child(uid).setValue(user1)

                    action(uid)
                }
            }

        }
    }

    private fun action(uid:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    private  fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener(this){
            task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Email enviado", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Error al enviar", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(view: View) {
        startActivity(Intent(this, Login::class.java))
    }



    fun Google(view: View) {
        val name: String=txtName.text.toString()
        val lastName: String=txtLastName.text.toString()


        if(!TextUtils.isEmpty((name))&& !TextUtils.isEmpty(lastName)){
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build())
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                    RC_SIGN_IN
            )
        }else{
            Toast.makeText(this,"Debes introducir tu nombre y apellido", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val name: String=txtName.text.toString()
                val lastName: String=txtLastName.text.toString()

                val user:FirebaseUser?=auth.currentUser
                val uid= user?.uid!!
                verifyEmail(user)

                //val user1 = User(name, lastName,"0","0")
                //val user1 = User(name, lastName)
                //val userBD= dbReference.child(uid).setValue(user1)

                action(uid)
            } else {
                Toast.makeText(this,"Error, no pudo registrarse", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(@Nullable account: FirebaseUser?) {
        if (account != null) {
            startActivity(Intent(this,MainActivity::class.java))
        } else {
            Toast.makeText(this,"Error, no pudo registrarse", Toast.LENGTH_LONG).show()
        }
    }

    fun Anonymous(view: View) {
        auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

}