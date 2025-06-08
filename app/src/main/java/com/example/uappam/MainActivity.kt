package com.example.uappam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Views
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterLink = findViewById<TextView>(R.id.tvRegisterLink)

        // Set klik tombol Login
        btnLogin.setOnClickListener {
            // Navigasi ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set klik link Register
        tvRegisterLink.setOnClickListener {
            // Navigasi ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}