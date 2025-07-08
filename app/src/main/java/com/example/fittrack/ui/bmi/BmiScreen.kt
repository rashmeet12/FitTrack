package com.example.fittrack.ui.bmi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun BmiScreen(navController: NavController, viewModel: BmiViewModel = hiltViewModel()) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (m)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val h = height.toDoubleOrNull() ?: 0.0
            val w = weight.toDoubleOrNull() ?: 0.0
            if (h > 0 && w > 0) {
                viewModel.calculateAndSave(h, w)
            }
        }) {
            Text("Calculate BMI")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("BMI: ${String.format("%.2f", viewModel.bmiResult)}", style = MaterialTheme.typography.headlineMedium)
    }
}
