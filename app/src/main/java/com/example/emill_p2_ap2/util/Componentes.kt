package com.example.emill_p2_ap2.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.emill_p2_ap2.data.local.model.Suplidor
import com.example.emill_p2_ap2.ui.gastoUi.GastoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SaveButton(viewModel: GastoViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedButton(
        onClick = {
            viewModel.postGasto()
            keyboardController?.hide()
        }, modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle, contentDescription = "Guardar"
            )
            Text(text = "Guardar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isValid: Boolean,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = if (isValid) Color.Black else Color.Red
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isValid) Color.Gray else Color.Red,
            unfocusedBorderColor = if (isValid) Color.Gray else Color.Red,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNumericalOutlinedTextField(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    isValid: Boolean,
    onValueChange: (Int) -> Unit,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            val numericValue = newValue.filter { it.isDigit() }
            onValueChange(numericValue.toIntOrNull() ?: 0)
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = if (isValid) Color.Black else Color.Red
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isValid) Color.Gray else Color.Red,
            unfocusedBorderColor = if (isValid) Color.Gray else Color.Red,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = imeAction
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNumericalOutlinedTextFieldDouble(
    label: String,
    value: Double,
    modifier: Modifier = Modifier,
    isValid: Boolean,
    onValueChange: (Double) -> Unit,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            val numericValue = newValue.filter { it.isDigit() || it == '.' }
            if (numericValue.isNotBlank()) {

                val doubleValue = numericValue.toDoubleOrNull()
                if (doubleValue != null) {
                    onValueChange(doubleValue)
                }
            } else {
                onValueChange(0.0)
            }
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(color = if (isValid) Color.Black else Color.Red),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isValid) Color.Gray else Color.Red,
            unfocusedBorderColor = if (isValid) Color.Gray else Color.Red,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = imeAction
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuplidorDropdown(
    suplidores: List<Suplidor>,
    selectedSuplidorId: Int,
    onSuplidorSelected: (Int) -> Unit
) {
    val options = suplidores
    var expanded by remember { mutableStateOf(false) }
    var selectedSuplidor = suplidores.find { it.id == selectedSuplidorId }
    var selectedOptionText by remember { mutableStateOf(selectedSuplidor?.nombre ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedOptionText,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(text = "Suplidor") },
            readOnly = true,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { suplidor ->
                DropdownMenuItem(
                    text = { Text(suplidor.nombre) },
                    onClick = {
                        selectedOptionText = suplidor.nombre
                        onSuplidorSelected(suplidor.id)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

