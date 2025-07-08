package com.example.fittrack.ui.bmi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.repository.BmiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BmiViewModel @Inject constructor(
    private val bmiRepo: BmiRepository,
    private val currentUserPref: CurrentUserPreference
) : ViewModel() {
    var bmiResult by mutableStateOf(0.0)
        private set

    fun calculateAndSave(height: Double, weight: Double) {
        if (height > 0) {
            val bmi = weight / (height * height)
            bmiResult = bmi
            viewModelScope.launch(Dispatchers.IO) {
                val uid = currentUserPref.userIdFlow.first()
                uid?.let {
                    val time = System.currentTimeMillis()
                    bmiRepo.addBmi(it, weight, height, bmi, time)
                }
            }

//            viewModelScope.launch {
//                currentUserPref.userIdFlow.collect { uid ->
//                    uid?.let {
//                        val time = System.currentTimeMillis()
//                        bmiRepo.addBmi(it, weight, height, bmi, time)
//                    }
//                }
//            }
        }
    }
}
