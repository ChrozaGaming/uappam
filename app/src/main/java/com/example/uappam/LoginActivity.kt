package com.example.uappam

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize UI Components
        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        // Handle Login Button Clicks
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Validate inputs
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.error = "Masukkan email yang valid"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordField.error = "Password tidak boleh kosong"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            // Authenticate user
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login success, navigate to MainPageActivity
                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                    finish() // Close LoginActivity
                } else {
                    // Login failed
                    Toast.makeText(
                        this,
                        "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}