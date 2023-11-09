package com.example.emill_p2_ap2.ui.gastoUi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emill_p2_ap2.data.remote.dto.GastoDto
import com.example.emill_p2_ap2.data.repository.GastoRepository
import com.example.emill_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class GastoListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val gastos: List<GastoDto> = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GastoListState())
    val uiState: StateFlow<GastoListState> = _uiState.asStateFlow()

    //
    var idSuplidor by mutableStateOf(0)
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf(0.0)
    var monto by mutableStateOf(0.0)
    var fecha by mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

    var isValidFecha by mutableStateOf(true)
    var isValidIdSuplidor by mutableStateOf(true)
    var isValidSuplidor by mutableStateOf(true)
    var isValidConcepto by mutableStateOf(true)
    var isValidNcf by mutableStateOf(true)
    var isValidItbis by mutableStateOf(true)
    var isValidMonto by mutableStateOf(true)

    private fun isValid(): Boolean {
        isValidIdSuplidor = idSuplidor > 0
        isValidConcepto = concepto.isNotBlank()
        isValidNcf = ncf.isNotBlank()
        isValidItbis = itbis > 0.0
        isValidMonto = monto > 0.0

        if (!isValidIdSuplidor) println("IdSuplidor no es válido")
        if (!isValidConcepto) println("Concepto no es válido")
        if (!isValidNcf) println("NCF no es válido")
        if (!isValidItbis) println("ITBIS no es válido")
        if (!isValidMonto) println("Monto no es válido")

        return isValidFecha && isValidIdSuplidor && isValidSuplidor && isValidConcepto && isValidNcf && isValidItbis && isValidMonto
    }
    //


    init {
        loadGastos()
    }

    private fun loadGastos() {
        viewModelScope.launch {
            gastoRepository.getGastos().collect() { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                gastos = result.data ?: emptyList(), isLoading = false, error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.message ?: "Error desconocido", isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun postGasto() {
        viewModelScope.launch {
            if (isValid()) {
                println("Guardando gasto...")

                val fechaActual = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                val nuevoGastoDto = GastoDto(
                    fecha = fechaActual,
                    idSuplidor = idSuplidor,
                    concepto = concepto,
                    ncf = ncf,
                    itbis = itbis,
                    monto = monto
                )
                try {
                    val result = gastoRepository.postGasto(nuevoGastoDto)
                    if (result is Resource.Success) {
                        val gastoCreado = result.data
                        gastoCreado?.let {
                            val updatedGastos = _uiState.value.gastos + it
                            _uiState.update { state -> state.copy(gastos = updatedGastos) }
                            limpiar()
                            println("Gasto guardado!")
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            error = (result as Resource.Error).message ?: "Error desconocido"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = e.message ?: "Error desconocido")
                }

            } else {
                println("Datos de gasto no son válidos.")
            }
        }
    }

    fun deleteGasto(id: Int) {
        viewModelScope.launch {
            try {
                val result = gastoRepository.deleteGasto(id)
                if (result is Resource.Success) {
                    val updatedGastos = _uiState.value.gastos.filter { it.idGasto != id }
                    _uiState.update { state -> state.copy(gastos = updatedGastos) }
                    println("Gasto eliminado!")
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = (result as Resource.Error).message ?: "Error desconocido"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Error desconocido")
            }
        }
    }

    private fun limpiar() {
        fecha = ""
        idSuplidor = 0
        concepto = ""
        ncf = ""
        itbis = 0.0
        monto = 0.0
    }
}