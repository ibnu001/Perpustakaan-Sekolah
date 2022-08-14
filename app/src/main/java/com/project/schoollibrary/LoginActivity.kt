package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.project.schoollibrary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {

            etLoginEmail.doOnTextChanged { text, start, before, count -> etLEmail(null) }
            etLoginPassword.doOnTextChanged { text, start, before, count -> etLPassword(null) }

            btnLoginLogin.setOnClickListener {
                var email = etLoginEmail.text.toString()
                var password = etLoginPassword.text.toString()

                when {
                    email.isEmpty() && password.isEmpty() -> {
                        etLEmail("Please fill your email!!")
                        etLPassword("Please fill your password!!")
                        etLoginEmail.requestFocus()
                    }

                    email.isEmpty() -> {
                        etLEmail("Please fill your email!!")
                        etLoginEmail.requestFocus()
                    }

                    password.isEmpty() -> {
                        etLPassword("Tolong isi password anda!!")
                        etLoginPassword.requestFocus()
                    }

                    else -> {
                        Login(email, password)
                    }
                }
            }
        }

        binding.tvLoginSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun Login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, DashMemberActivity::class.java)
                    intent.putExtra("email", email)

                    startActivity(intent)
                    moveToDashMA()
                } else {
                    showToast("Incorrect email and password")
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            moveToDashMA()
        }
    }

    private fun etLEmail(note: String?) {
        binding.etLayoutLoginEmail.error = note
    }

    private fun etLPassword(note: String?) {
        binding.etLoginPassword.error = note
    }

    private fun moveToDashMA() {
        Intent(this, DashMemberActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun showToast(note: String) {
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
    }
}