package com.example.uappam

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.uappam.api.ApiClient
import com.example.uappam.models.DefaultResponse
import com.example.uappam.models.PlantRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateItemActivity : AppCompatActivity() {

    private lateinit var plantImage: ImageView
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: CardView
    private lateinit var originalName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateitem)

        plantImage = findViewById(R.id.plantUpdateImage)
        etName = findViewById(R.id.etPlantName)
        etPrice = findViewById(R.id.etPlantPrice)
        etDescription = findViewById(R.id.etPlantDescription)
        btnSave = findViewById(R.id.btnSave)

        originalName = intent.getStringExtra("PLANT_NAME") ?: ""
        val price = intent.getStringExtra("PLANT_PRICE") ?: ""
        val description = intent.getStringExtra("PLANT_DESCRIPTION") ?: ""

        etName.setText(originalName)
        etPrice.setText(price)
        etDescription.setText(description)

        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            val newPrice = etPrice.text.toString()
            val newDescription = etDescription.text.toString()

            val request = PlantRequest(newName, newPrice, newDescription)
            ApiClient.instance.updatePlant(originalName, request).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UpdateItemActivity, "Berhasil memperbarui tanaman", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorMsg = response.body()?.message ?: "Gagal memperbarui tanaman"
                        Toast.makeText(this@UpdateItemActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@UpdateItemActivity, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}