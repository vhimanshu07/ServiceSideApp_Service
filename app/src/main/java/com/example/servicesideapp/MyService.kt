package com.example.servicesideapp

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import kotlin.math.nextTowards

/**
 * Created by Himanshu Verma on 12/06/24.
 **/
class MyService : Service() {
    private var randomNumberGeneratorOn: Boolean = false
    private var randomNumber: Double = 0.0
    private val maxValue: Int = 100

    /**
     *
     * Step 3 making randing request handler
     *
     */
    var GET_RANDOM_NUMBER: Int = 0

    inner class RandomRequestHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            /**
             *
             * Step 4  msg.what is the FLAG what we would be getting from other apps
             *
             */
            when (msg.what) {
                GET_RANDOM_NUMBER -> {
                    val messageRandomNumber: Message = Message.obtain(null, GET_RANDOM_NUMBER)
                    messageRandomNumber.arg1 = getRandomNumber().toInt()
                    /**
                     *
                     * Step 5  Sending message back to sender.
                     *
                     */
                    try {
                        msg.replyTo.send(messageRandomNumber)
                    } catch (e: Exception) {

                    }
                }

            }
            super.handleMessage(msg)
        }

    }

    /**
     *
     * Step 6 Making random number messenger for handling incoming msg and sending reply
     *
     */

    val randomMessageMessenger: Messenger = Messenger(RandomRequestHandler())

    /**
     *
     * Step 7 Giving the binder of randomMessageMessenger
     *
     */
    override fun onBind(intent: Intent?): IBinder? {
        Log.d("Service", "On bind")
        if (intent?.`package`?.equals("com.example.clientsidebinderapp") == true) {
            Toast.makeText(applicationContext,"Right package",Toast.LENGTH_SHORT).show()
            return randomMessageMessenger.binder
        } else {
            Toast.makeText(applicationContext,"wrong package",Toast.LENGTH_SHORT).show()
            return null
        }

    }

    /**
     *
     * Step 2 copy paste whole service from servicewhole except onbind function
     *
     */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "Started and Thread id :- ${Thread.currentThread().id}")
        /**
         *
         * Step 6 random number
         *
         */
        randomNumberGeneratorOn = true
        Thread(
            object : Runnable {
                override fun run() {
                    startRandomGenerator()
                }

            }
        ).start()

        return START_STICKY
    }

    private fun startRandomGenerator() {
        while (randomNumberGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (randomNumberGeneratorOn) {
                    randomNumber = Math.random().nextTowards(maxValue.toDouble())
                    Log.d(
                        "Service",
                        "Started and Thread id :- ${Thread.currentThread().id} random number is : $randomNumber"
                    )
                }

            } catch (e: Exception) {
                Log.d(
                    "Service",
                    "Thread interrupted"
                )
            }
        }
    }

    private fun stopRandomGenerator() {
        randomNumberGeneratorOn = false

    }

    fun getRandomNumber(): Double {
        return randomNumber
    }

    override fun onDestroy() {
        Log.d("Service", "Destroyed")
        super.onDestroy()
        stopRandomGenerator()
    }
}