package com.example.emill_p2_ap2.data.remote.dto

import androidx.room.PrimaryKey

data class GastoDto(
    @PrimaryKey var idGasto: Int? = null,
    var fecha: String = "",
    var idSuplidor: Int? = null,
    var suplidor: String = "",
    var concepto: String = "",
    var ncf: String? = null,
    var itbis: Double = 0.0,
    var monto: Double = 0.0,
    var descuento: Int = 0
)