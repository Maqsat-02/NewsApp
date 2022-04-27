package kz.iitu.newsapp.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kz.iitu.newsapp.R
import kz.iitu.newsapp.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile){


    //ActionBar
    private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configure
        actionBar = getActionbar()!!
        actionBar.title = "Profile"

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }
    fun getActionbar() : ActionBar?
    {
        return (activity as AppCompatActivity).supportActionBar
    }
    private fun checkUser() {
        var firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            var email = firebaseUser.email
            emailTv.text = email
        }
            activity?.let{
                val intent = Intent (it, LoginActivity::class.java)
                it.startActivity(intent)
        }
    }
}