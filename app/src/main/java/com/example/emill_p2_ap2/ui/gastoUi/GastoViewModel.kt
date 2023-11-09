package com.example.emill_p2_ap2.ui.gastoUiÇ
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

    private val _uiState = MutableStateFlow(GastoListState())
    val uiState: StateFlow<GastoListState> = _uiState.asStateFlow()

    init {
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
}