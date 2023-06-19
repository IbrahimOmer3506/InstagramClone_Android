package com.oztasibrahimomer.instagramkotlin2.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oztasibrahimomer.instagramkotlin2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.visibility=View.INVISIBLE


        myAuth= Firebase.auth  // initilaze edildi!!!




        val currentUser=myAuth.currentUser

        if(currentUser!=null){

            val intent=Intent(this@MainActivity, FeedActivity::class.java)

            startActivity(intent)

        }




    }



    fun signUp(view: View){
        // kullanici kaydi

        val email=binding.emailText.text.toString()
        val password=binding.passwordText.text.toString()

        if(email.equals("")||password.equals("")){

            Toast.makeText(this,"enter email or password",Toast.LENGTH_LONG).show()
        }
        else{

            myAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                object:CountDownTimer(3000,1000){
                    override fun onTick(p0: Long) {
                        binding.progressBar.visibility=View.VISIBLE
                    }

                    override fun onFinish() {
                        val intent = Intent(this@MainActivity, FeedActivity::class.java)
                        finish()
                        startActivity(intent)
                    }

                }.start()
            }.addOnFailureListener {e->

                Toast.makeText(this@MainActivity,e.localizedMessage,Toast.LENGTH_LONG).show()

            }

        }





    }
    fun signIn(view:View){
        //kullanici girisi

        val email=binding.emailText.text.toString()
        val password=binding.passwordText.text.toString()

        if(email.equals("")||password.equals("")){

            Toast.makeText(this,"Enter email or password",Toast.LENGTH_LONG).show()


        }
        else{
            myAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                object:CountDownTimer(3000,1000){
                    override fun onTick(p0: Long) {
                        binding.progressBar.visibility=View.VISIBLE
                    }

                    override fun onFinish() {
                        val intent=Intent(this@MainActivity, FeedActivity::class.java)
                        finish()
                        startActivity(intent)
                    }

                }.start()

            }.addOnFailureListener { e->
                Toast.makeText(this@MainActivity,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
}