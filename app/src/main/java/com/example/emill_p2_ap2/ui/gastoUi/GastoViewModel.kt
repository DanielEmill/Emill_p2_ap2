package com.example.emill_p2_ap2.ui.gastoUi

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
import javax.inject.Inject

data class GastoListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val gastos: List<GastoDto> = emptyList()
)

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository
) : ViewModel() {

    //
    var fecha by mutableStateOf("")
    var idSuplidor by mutableStateOf(0)
    var suplidor by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf(0.0)
    var monto by mutableStateOf(0.0)

    var isValidFecha by mutableStateOf(true)
    var isValidIdSuplidor by mutableStateOf(true)
    var isValidSuplidor by mutableStateOf(true)
    var isValidConcepto by mutableStateOf(true)
    var isValidNcf by mutableStateOf(true)
    var isValidItbis by mutableStateOf(true)
    var isValidMonto by mutableStateOf(true)

    //
    private val _uiState = MutableStateFlow(GastoListState())
    val uiState: StateFlow<GastoListState> = _uiState.asStateFlow()

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
                                gastos = result.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isValid(): Boolean {
        isValidFecha = fecha.isNotBlank()
        isValidIdSuplidor = idSuplidor > 0
        isValidSuplidor = suplidor.isNotBlank()
        isValidConcepto = concepto.isNotBlank()
        isValidNcf = ncf.isNotBlank()
        isValidItbis = itbis > 0.0
        isValidMonto = monto > 0.0

        return isValidFecha && isValidIdSuplidor && isValidSuplidor && isValidConcepto && isValidNcf && isValidItbis && isValidMonto
    }

    fun addGasto(gastoDto: GastoDto) {
        viewModelScope.launch {
            if (isValid()) {
                gastoRepository.postGasto(gastoDto)
                limpiar()
            }
        }
    }


    private fun limpiar() {
        fecha = ""
        idSuplidor = 0
        suplidor = ""
        concepto = ""
        ncf = ""
        itbis = 0.0
        monto = 0.0
    }


}