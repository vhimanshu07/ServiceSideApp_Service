package com.example.servicesideapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.servicesideapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var serviceIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Service", packageName)

        /**
         *
         * Step 1 make buttons in xml and go to service
         *
         */

        /**
         *
         * Step 8 click handling
         *
         */
        serviceIntent = Intent(applicationContext, MyService::class.java)
        binding.apply {
            start.setOnClickListener {
                startService(serviceIntent)
            }
            stopBtn.setOnClickListener {
                stopService(serviceIntent)
            }
        }
        /**
         *
         * Step 9 Make ClientSideBinderApp and make changes in manifest file and make service
         * exported = true
         *
         */
    }
}