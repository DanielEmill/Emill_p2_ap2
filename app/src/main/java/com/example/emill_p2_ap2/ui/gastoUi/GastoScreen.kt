package com.example.emill_p2_ap2.ui.gastoUi

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emill_p2_ap2.data.local.model.suplidores
import com.example.emill_p2_ap2.data.remote.dto.GastoDto
import com.example.emill_p2_ap2.util.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreen(gastoViewModel: GastoViewModel = viewModel()) {
    val uiState by gastoViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(title = { Text("Gastos API") })
    }) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
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
                    RegistroGastoScreen(gastoViewModel)
                    SaveButton(gastoViewModel)
                    GastoDetails(gastoList = uiState.gastos)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroGastoScreen(viewModel: GastoViewModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SuplidorDropdown(
            suplidores = suplidores,
            selectedSuplidorId = viewModel.idSuplidor,
            onSuplidorSelected = { selectedSuplidorId ->
                viewModel.idSuplidor = selectedSuplidorId
            }
        )
        CustomOutlinedTextField(
            label = "Concepto",
            value = viewModel.concepto,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidConcepto,
            onValueChange = { viewModel.concepto = it },
            imeAction = ImeAction.Next
        )
        CustomOutlinedTextField(
            label = "NCF",
            value = viewModel.ncf,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidNcf,
            onValueChange = { viewModel.ncf = it },
            imeAction = ImeAction.Next
        )
        CustomNumericalOutlinedTextFieldDouble(
            label = "ITBIS",
            value = viewModel.itbis,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidItbis,
            onValueChange = { viewModel.itbis = it },
            imeAction = ImeAction.Next
        )
        CustomNumericalOutlinedTextFieldDouble(
            label = "Monto",
            value = viewModel.monto,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidMonto,
            onValueChange = { viewModel.monto = it },
            imeAction = ImeAction.Done
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GastoDetails(gastoList: List<GastoDto>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(gastoList) { gasto ->
            val fechaParseada = LocalDateTime.parse(gasto.fecha, DateTimeFormatter.ISO_DATE_TIME)
            val fechaFormateada = fechaParseada.format(DateTimeFormatter.ISO_DATE)

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
                                "ITBIS: ${gasto.itbis}", style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "$${gasto.monto}",
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
