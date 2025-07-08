package com.example.fittrack.ui.history

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.data.model.ActivityEntity
import com.example.fittrack.data.model.BmiEntity
import com.example.fittrack.data.model.StepEntity
import com.example.fittrack.repository.ActivityRepository
import com.example.fittrack.repository.BmiRepository
import com.example.fittrack.repository.StepRepository
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val stepRepo: StepRepository,
    private val bmiRepo: BmiRepository,
    private val activityRepo: ActivityRepository,
    private val currentUserPref: CurrentUserPreference
) : ViewModel() {
    val stepsHistory = mutableStateListOf<StepEntity>()
    val bmiHistory = mutableStateListOf<BmiEntity>()
    val activities = mutableStateListOf<ActivityEntity>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserPref.userIdFlow.collect { uid ->
                uid?.let {
                    val steps = stepRepo.getSteps(it)
                    val bmiRecords = bmiRepo.getBmiRecords(it)
                    val activityList = activityRepo.getActivities(it)
                    withContext(Dispatchers.Main) {
                        stepsHistory.clear()
                        stepsHistory.addAll(steps)
                        bmiHistory.clear()
                        bmiHistory.addAll(bmiRecords)
                        activities.clear()
                        activities.addAll(activityList)
                    }
                }
            }
        }

    }

    // Prepare MPAndroidChart data
    fun getStepsLineData(): LineData {
        val entries = stepsHistory.mapIndexed { i, step ->
            Entry(i.toFloat(), step.count.toFloat())
        }
        val dataSet = LineDataSet(entries, "Daily Steps")
        return LineData(dataSet)
    }
    fun getBmiLineData(): LineData {
        val entries = bmiHistory.mapIndexed { i, record ->
            Entry(i.toFloat(), record.bmiValue.toFloat())
        }
        val dataSet = LineDataSet(entries, "BMI")
        return LineData(dataSet)
    }
}
