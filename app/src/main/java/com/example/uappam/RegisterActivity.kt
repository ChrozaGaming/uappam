package com.example.uappam

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.etConfirmPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        // Handle Register button clicks
        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            // Validation logic
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.error = "Masukkan email yang valid"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                passwordField.error = "Password minimal terdiri dari 6 karakter"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordField.error = "Password tidak sesuai"
                confirmPasswordField.requestFocus()
                return@setOnClickListener
            }

            // Call Firebase Authentication function
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_LONG).show()
                    finish() // Close current activity
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}