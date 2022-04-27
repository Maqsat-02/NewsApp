package kz.iitu.newsapp.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kz.iitu.newsapp.databinding.ActivitySignUpBinding
import kz.iitu.newsapp.ui.NewsActivity

class SignUpActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivitySignUpBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    var email = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure
        actionBar = supportActionBar!!
        actionBar.title = "Sign up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progress
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.singUpBtn.setOnClickListener {
            validateData()
        }
        binding.haveAccountTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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
        else if(password.length<6){
            binding.passwordEt.error = "Password must be at least 6 characters long"
        }
        else {
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                var firebaseUser = firebaseAuth.currentUser
                var email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, NewsActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}