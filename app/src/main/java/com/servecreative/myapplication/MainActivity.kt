package com.servecreative.myapplication

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.servecreative.myapplication.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.servecreative.myapplication.login
import com.servecreative.myapplication.sendbird.utils.AuthenticationUtils

class MainActivity : AppCompatActivity() {

    private val MANDATORY_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,  // for VoiceCall and VideoCall
        Manifest.permission.CAMERA, // for VideoCall
        Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    val PERMISSION_DENIED ="Permission Denied"
    public val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
//        Handler().postDelayed({ startActivity(Intent(this@MainActivity, login::class.java)) }, 2000)



    }

    fun sendBirdCallAuthentication() {
//        added from SignInManuallyActivity
        AuthenticationUtils.authenticate(this, "vicky", "") { isSuccess ->

            try {
                if (isSuccess) {
                    Toast.makeText(this, "vicky authenticated successfully", Toast.LENGTH_SHORT).show()

                    setResult(RESULT_OK, null)
                    startActivity(Intent(this@MainActivity, login::class.java))
                }
                else
                {
                    Toast.makeText(this, "vicky authenticated failed..", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity_", "sendBirdCallAuthentication - $e")

            }
        }
    }

    private fun checkPermissions() {
        val deniedPermissions = ArrayList<String>()
        for (permission in MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission)
            }
        }
        if (deniedPermissions.size > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    deniedPermissions.toTypedArray(),
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )

            } else {
                dialogWithOneButton(PERMISSION_DENIED)
            }
        } else {
            startActivity(Intent(this@MainActivity, login::class.java))
//            sendBirdCallAuthentication()
//            startActivity(Intent(this@MainActivity, login::class.java))
            //   makecall()
//            dialogWithOneButton1(Comman.getMessage().callMessage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        try {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
                var allowed = true
                for (result in grantResults) {
                    allowed = allowed && result == PackageManager.PERMISSION_GRANTED
                }
                if (!allowed) {
                    dialogWithOneButton(PERMISSION_DENIED)
                } else {
                    startActivity(Intent(this@MainActivity, login::class.java))
//                    sendBirdCallAuthentication()
//                    dialogWithOneButton1(Comman.getMessage().callMessage)
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity_", "onRequestPermissionsResult - $e")
        }
    }

    fun dialogWithOneButton(message: String) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_with_one_button, viewGroup, false)

        val tvMessage = dialogView.findViewById<TextView>(R.id.tv_message)
        val tvBtn = dialogView.findViewById<TextView>(R.id.tv_btn)

        tvMessage.text = message

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.show()

        tvBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
    }
}