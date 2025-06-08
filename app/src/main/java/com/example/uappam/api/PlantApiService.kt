package com.example.uappam.api

import com.example.uappam.models.DefaultResponse
import com.example.uappam.models.PlantRequest
import com.example.uappam.models.PlantResponse
import com.example.uappam.models.SinglePlantResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlantApiService {
    @GET("plant/all")
    fun getAllPlants(): Call<PlantResponse>

    @GET("plant/{name}")
    fun getPlantByName(@Path("name") name: String): Call<SinglePlantResponse>

    @POST("plant/new")
    fun addPlant(@Body request: PlantRequest): Call<DefaultResponse>

    @PUT("plant/{name}")
    fun updatePlant(@Path("name") originalName: String, @Body request: PlantRequest): Call<DefaultResponse>

    @DELETE("plant/{name}")
    fun deletePlant(@Path("name") name: String): Call<DefaultResponse>
}
