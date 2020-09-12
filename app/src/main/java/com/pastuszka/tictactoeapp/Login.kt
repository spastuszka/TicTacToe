package com.pastuszka.tictactoeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
    }

    fun buLoginEvent(view: View){
        LoginToFirebase(etEmail.text.toString(), etPassword.text.toString())
    }

    fun LoginToFirebase(email:String,password:String){
        
        mAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->

                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Pomyslne logowanie",Toast.LENGTH_LONG).show()
                    LoadMain()

                }else{
                    Toast.makeText(applicationContext,"Blad logowania",Toast.LENGTH_LONG).show()
                }
                
            }
        
    }

    override fun onStart() {
        super.onStart()
        LoadMain()
    }

    fun LoadMain(){

        var currentUser = mAuth!!.currentUser

            if(currentUser!=null) {

                //save in database
                myRef.child("Users").child(currentUser.uid).setValue(currentUser.email)
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", currentUser.email)
                intent.putExtra("uid", currentUser.uid)

                startActivity(intent)
            }

    }
}
