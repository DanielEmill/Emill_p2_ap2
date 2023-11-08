package com.example.emill_p2_ap2.data.remote

import com.example.emill_p2_ap2.data.remote.dto.GastoDto
import retrofit2.Response
import retrofit2.http.*

interface GastoApi {

    @GET("/api/Gastos")
    fun getGastos(): List<GastoDto>

    @GET("/api/Gastos/{id}")
    fun getGastoId(@Path("id") id: Int): GastoDto

    @POST("/api/Gastos")
    fun postGasto(@Body gastoDto: GastoDto): Response<GastoDto>

    @PUT("/api/Gastos/{id}")
    fun putGasto(@Path("id") id: Int, @Body gastoDto: GastoDto): Response<Unit>

    @DELETE("/api/Gastos/{id}")
    fun deleteGasto(@Path("id") id: Int): Response<Unit>
}