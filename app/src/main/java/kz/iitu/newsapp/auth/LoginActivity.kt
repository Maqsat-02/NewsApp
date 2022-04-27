package kz.iitu.newsapp.auth

import android.app.ActionBar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kz.iitu.newsapp.databinding.ActivityLoginBinding
import kz.iitu.newsapp.ui.NewsActivity
import kz.iitu.newsapp.ui.fragments.BreakingNewsFragment
import kz.iitu.newsapp.ui.fragments.ProfileFragment

import java.util.regex.Pattern

class LoginActivity: AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: androidx.appcompat.app.ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open SignUpActivity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        //handle click, begin login
        binding.loginBtn.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.error = "Please enter password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()

                var firebaseUser = firebaseAuth.currentUser
                var email = firebaseUser!!.email
                Toast.makeText(this, "Logged as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, NewsActivity::class.java))
                finish()

            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user already logged in go to profile activity
        //get current User
        var firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }
    }
}