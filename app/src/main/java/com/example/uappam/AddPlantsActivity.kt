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

class AddPlantsActivity : AppCompatActivity() {

    private lateinit var plantImage: ImageView
    private lateinit var etPlantName: EditText
    private lateinit var etPlantPrice: EditText
    private lateinit var etPlantDescription: EditText
    private lateinit var btnAdd: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplants)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        plantImage = findViewById(R.id.plantAddImage)
        etPlantName = findViewById(R.id.etAddPlantName)
        etPlantPrice = findViewById(R.id.etAddPlantPrice)
        etPlantDescription = findViewById(R.id.etAddPlantDescription)
        btnAdd = findViewById(R.id.btnAdd)
    }

    private fun setupClickListeners() {
        btnAdd.setOnClickListener {
            validateAndSave()
        }

        plantImage.setOnClickListener {
            Toast.makeText(this, "Fitur upload gambar belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndSave() {
        val name = etPlantName.text.toString().trim()
        val price = etPlantPrice.text.toString().trim()
        val description = etPlantDescription.text.toString().trim()

        if (name.isEmpty()) {
            etPlantName.error = "Nama tanaman harus diisi"
            etPlantName.requestFocus()
            return
        }
        if (price.isEmpty()) {
            etPlantPrice.error = "Harga harus diisi"
            etPlantPrice.requestFocus()
            return
        }
        if (description.isEmpty()) {
            etPlantDescription.error = "Deskripsi harus diisi"
            etPlantDescription.requestFocus()
            return
        }

        val plantRequest = PlantRequest(
            plant_name = name,
            price = price,
            description = description
        )

        ApiClient.instance.addPlant(plantRequest).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@AddPlantsActivity, "Tanaman berhasil ditambahkan!", Toast.LENGTH_LONG).show()
                    clearForm()
                    finish()
                } else {
                    val errorMsg = response.body()?.message ?: "Gagal menambahkan tanaman"
                    Toast.makeText(this@AddPlantsActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@AddPlantsActivity, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearForm() {
        etPlantName.text.clear()
        etPlantPrice.text.clear()
        etPlantDescription.text.clear()
        etPlantName.requestFocus()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
