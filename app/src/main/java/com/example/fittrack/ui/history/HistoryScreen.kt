package com.example.fittrack.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.LineChart

@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Steps History", style = MaterialTheme.typography.headlineMedium)
        AndroidView(factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                data = viewModel.getStepsLineData()
                invalidate()
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("BMI Trends", style = MaterialTheme.typography.headlineMedium)
        AndroidView(factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                data = viewModel.getBmiLineData()
                invalidate()
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Past Activities", style = MaterialTheme.typography.headlineMedium)
        // List activities
        LazyColumn {
            items(viewModel.activities) { activity ->
                Text("${activity.name}: ${activity.startTime} - ${activity.endTime}")
            }
        }
    }
}
