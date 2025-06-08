package com.example.uappam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uappam.adapters.PlantAdapter
import com.example.uappam.api.ApiClient
import com.example.uappam.models.Plant
import com.example.uappam.models.PlantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var btnAddPlant: CardView
    private var plantList: MutableList<Plant> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        recyclerView = findViewById(R.id.recyclerViewPlants)
        btnAddPlant = findViewById(R.id.btnAddPlant)

        setupRecyclerView()
        setupButtonClickListener()
        fetchPlantsFromApi()
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantAdapter(this, plantList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainPageActivity)
            adapter = plantAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupButtonClickListener() {
        btnAddPlant.setOnClickListener {
            val intent = Intent(this, AddPlantsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPlantsFromApi() {
        ApiClient.instance.getAllPlants().enqueue(object : Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        plantList.clear()
                        plantList.addAll(responseData.data)
                        plantAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@MainPageActivity, "Gagal memuat data! (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                Toast.makeText(this@MainPageActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchPlantsFromApi() // Refresh saat kembali ke halaman ini
    }
}
