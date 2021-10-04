 package com.example.e_commerceshopapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.e_commerceshopapp.databinding.ActivitySplashBinding
import com.example.e_commerceshopapp.firestore.FireStoreClass

 @SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                val currentUserID = FireStoreClass().getCurrentUserID()

                if (currentUserID.isNotEmpty()) {

                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                }else{

                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }

            },2500
        )

    }
}