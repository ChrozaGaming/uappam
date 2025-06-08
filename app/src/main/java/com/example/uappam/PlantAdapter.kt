package com.example.uappam.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.uappam.R
import com.example.uappam.DetailsPlantActivity
import com.example.uappam.api.ApiClient
import com.example.uappam.models.DefaultResponse
import com.example.uappam.models.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantAdapter(
    private val context: Context,
    private var plants: MutableList<Plant>
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]

        holder.imgPlant.setImageResource(R.drawable.plant_placeholder)
        holder.tvName.text = plant.plant_name
        holder.tvPrice.text = "Rp ${plant.price}"

        // Tombol Hapus
        holder.btnDelete.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                ApiClient.instance.deletePlant(plant.plant_name).enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody != null) {
                            if (responseBody.success == true) {
                                Toast.makeText(context, "Tanaman berhasil dihapus", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Tanaman dihapus dapat dihapus", Toast.LENGTH_SHORT).show()
                            }

                            // Tetap hapus dari list meskipun success bernilai null/false tapi status 200
                            plants.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                            notifyItemRangeChanged(currentPosition, plants.size)

                        } else {
                            Toast.makeText(context, "Gagal menghapus tanaman (kode ${response.code()})", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // Tombol Detail
        holder.btnDetail.setOnClickListener {
            val intent = Intent(context, DetailsPlantActivity::class.java).apply {
                putExtra("PLANT_NAME", plant.plant_name)
                putExtra("PLANT_PRICE", plant.price)
                putExtra("PLANT_IMAGE", R.drawable.plant_placeholder)
                putExtra("PLANT_DESCRIPTION", plant.description)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = plants.size

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPlant: ImageView = itemView.findViewById(R.id.plantImage)
        val tvName: TextView = itemView.findViewById(R.id.plantName)
        val tvPrice: TextView = itemView.findViewById(R.id.plantPrice)
        val btnDelete: CardView = itemView.findViewById(R.id.btnDelete)
        val btnDetail: CardView = itemView.findViewById(R.id.btnDetail)
    }

    fun updateData(newList: List<Plant>) {
        plants = newList.toMutableList()
        notifyDataSetChanged()
    }
}
