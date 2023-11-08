package com.example.emill_p2_ap2.data.repository

import com.example.emill_p2_ap2.data.remote.GastoApi
import com.example.emill_p2_ap2.data.remote.dto.GastoDto
import com.example.emill_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GastoRepository @Inject constructor(
    private val api: GastoApi
) {
    fun getGastos(): Flow<Resource<List<GastoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val gastos = api.getGastos()
            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    fun getGastoId(id: Int): Flow<Resource<GastoDto>> = flow {
        try {
            emit(Resource.Loading())
            val gasto = api.getGastoId(id)
            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    suspend fun putGasto(id: Int, gastoDto: GastoDto): Resource<Unit> {
        return try {
            val response = api.putGasto(id, gastoDto)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error PUT Gasto")
            }
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            Resource.Error(e.message ?: "verificar tu conexion a internet")
        }
    }

    suspend fun deleteGasto(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteGasto(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error DELETE Gasto")
            }
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            Resource.Error(e.message ?: "verificar tu conexion a internet")
        }
    }

    suspend fun postGasto(gastoDto: GastoDto): Resource<GastoDto> {
        return try {
            val response = api.postGasto(gastoDto)
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error POST Gasto")
            }
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            Resource.Error(e.message ?: "verificar tu conexion a internet")
        }
    }
}