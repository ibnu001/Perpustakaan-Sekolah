package com.project.schoollibrary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.schoollibrary.databinding.ActivitySplashScreenBinding
import kotlin.math.log

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnScAdmin.setOnClickListener {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }

            btnScMember.setOnClickListener {
                logout()
                startActivity(Intent(this@SplashScreenActivity, DashMemberActivity::class.java))
            }
        }
    }

    private fun logout() {
        if (true) {
            Firebase.auth.signOut()
        }
    }
}