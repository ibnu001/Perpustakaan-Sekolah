package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.project.schoollibrary.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        binding.apply {

            etSignupFullName.doOnTextChanged { text, start, before, count -> etLFullName(null) }
            etSignupEmail.doOnTextChanged { text, start, before, count -> etLEmail(null) }
            etSignupPassword.doOnTextChanged { text, start, before, count -> etLPassword(null) }

            btnSignupCreate.setOnClickListener {
                var fullName = etSignupFullName.text.toString()
                var email = etSignupEmail.text.toString()
                var password = etSignupPassword.text.toString()

                when {
                    fullName.isEmpty() && email.isEmpty() && password.isEmpty() -> {
                        etLFullName("Please fill your full name!!")
                        etLEmail("Please fill your email!!")
                        etLPassword("Please fill your password!!")
                        etSignupFullName.requestFocus()
                    }

                    fullName.isEmpty() -> {
                        etLFullName("Please fill your full name!!")
                        etSignupFullName.requestFocus()
                    }

                    email.isEmpty() -> {
                        etLEmail("Please fill your email!!")
                        etSignupEmail.requestFocus()
                    }

                    password.isEmpty() -> {
                        etLPassword("Tolong isi password anda!!")
                        etSignupPassword.requestFocus()
                    }

                    else -> {
                        signUp(email, password)
                    }
                }
            }
        }

        binding.tvSignupLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Auth()

                    showToast("Success")
                }
                else {
                    showToast("Failed")
                }
            }
    }

    private fun Auth() {
        var fullName = binding.etSignupFullName.text.toString()
        var email = binding.etSignupEmail.text.toString()
        var password = binding.etSignupPassword.text.toString()

        var uid = auth.uid.toString()

        mDbRef.child("user").child(uid)
            .setValue(SignUpAuthModel(uid, fullName, email, password))

        logout()

        binding.etSignupFullName.text?.clear()
        binding.etSignupEmail.text?.clear()
        binding.etSignupPassword.text?.clear()
    }

    private fun etLFullName(note: String?) {
        binding.etLayoutSignupFullName.error = note
    }

    private fun etLEmail(note: String?) {
        binding.etLayoutSignupEmail.error = note
    }

    private fun etLPassword(note: String?) {
        binding.etLayoutSignupPassword.error = note
    }

    private fun showToast(note: String) {
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        if (true) {
            Firebase.auth.signOut()
        }
    }
}