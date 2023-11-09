package com.example.emill_p2_ap2.ui.gastoUi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emill_p2_ap2.data.remote.dto.GastoDto
import com.example.emill_p2_ap2.util.*
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreen(gastoViewModel: GastoViewModel = viewModel()) {
    val uiState by gastoViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gastos API") }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Text("Error: ${uiState.error}")
            }

            uiState.gastos != null -> {
                Column {
                    Spacer(modifier = Modifier.height(padding.calculateTopPadding()))
                    GastoDetails(gastoList = uiState.gastos)
                }
            }
        }
    }
}

@Composable
fun GastoDetails(gastoList: List<GastoDto>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(gastoList) { gasto ->
            val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatoNuevo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaParseada = formatoOriginal.parse(gasto.fecha)
            val fechaFormateada = formatoNuevo.format(fechaParseada)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Id: ${gasto.idGasto}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(3f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "$fechaFormateada",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("${gasto.suplidor}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${gasto.concepto}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("NCF: ${gasto.ncf}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "ITBIS: ${gasto.itbis}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Monto: ${gasto.monto}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
