package com.example.uappam

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.uappam.api.ApiClient
import com.example.uappam.models.SinglePlantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsPlantActivity : AppCompatActivity() {

    private lateinit var imgPlant: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnUpdate: CardView
    private var plantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailsplant)

        imgPlant = findViewById(R.id.plantDetailImage)
        tvName = findViewById(R.id.plantDetailName)
        tvPrice = findViewById(R.id.plantDetailPrice)
        tvDescription = findViewById(R.id.plantDetailDescription)
        btnUpdate = findViewById(R.id.btnUpdate)

        plantName = intent.getStringExtra("PLANT_NAME") ?: ""

        if (plantName.isEmpty()) {
            Toast.makeText(this, "Data tanaman tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchPlantDetails(plantName)

        btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateItemActivity::class.java)
            intent.putExtra("PLANT_NAME", tvName.text.toString())
            intent.putExtra("PLANT_PRICE", tvPrice.text.toString().replace("Rp ", ""))
            intent.putExtra("PLANT_DESCRIPTION", tvDescription.text.toString())
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val updatedName = data?.getStringExtra("PLANT_NAME") ?: return
            fetchPlantDetails(updatedName)
        }
    }

    private fun fetchPlantDetails(name: String) {
        ApiClient.instance.getPlantByName(name).enqueue(object : Callback<SinglePlantResponse> {
            override fun onResponse(call: Call<SinglePlantResponse>, response: Response<SinglePlantResponse>) {
                if (response.isSuccessful) {
                    val plant = response.body()?.data
                    if (plant != null) {
                        tvName.text = plant.plant_name
                        tvPrice.text = "Rp ${plant.price}"
                        tvDescription.text = plant.description
                        imgPlant.setImageResource(R.drawable.plant_placeholder)
                    }
                } else {
                    Toast.makeText(this@DetailsPlantActivity, "Gagal memuat detail tanaman", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SinglePlantResponse>, t: Throwable) {
                Toast.makeText(this@DetailsPlantActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
