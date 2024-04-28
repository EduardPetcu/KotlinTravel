package com.example.travel.components.DesignComponents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Calendar
// TODO: Show another dialog on dismiss of the first dialog to check if the user wants to save the date range

class DateRange {

    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    fun SelectIntervalDate(onDoneClick: () -> Unit) {
        val state = rememberDateRangePickerState()
        var showDialog by remember { mutableStateOf(true) }
        Log.d("SelectIntervalDate", "Show dialog is: $showDialog")

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false
                onDoneClick()
            }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    DateRangePickerSample(state)
                    Button(
                        onClick = {
                            showDialog = false
                            startDate = getFormattedDate(state.selectedStartDateMillis!!)
                            endDate = getFormattedDate(state.selectedEndDateMillis!!)
                            onDoneClick()
                                  },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp),
                        enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            }
        }
    }

    fun getFormattedDate(timeInMillis: Long): String {
        val calender = Calendar.getInstance()
        calender.timeInMillis = timeInMillis
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calender.timeInMillis)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DateRangePickerSample(state: DateRangePickerState) {
        if (state.selectedStartDateMillis != null && state.selectedEndDateMillis != null) {
            startDate = getFormattedDate(state.selectedStartDateMillis!!)
            endDate = getFormattedDate(state.selectedEndDateMillis!!)
        }
        Log.d("DateRange", "Start Date: $startDate")
        DateRangePicker(
            state,
            modifier = Modifier,
            title = {
                Text(
                    text = "Select date range for your trip!", modifier = Modifier
                        .padding(16.dp)
                )
            },
            headline = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(Modifier.weight(0.5f)) {
                        (if (state.selectedStartDateMillis != null) state.selectedStartDateMillis?.let {
                            getFormattedDate(
                                it
                            )
                        } else "Start Date")?.let { Text(text = it) }
                    }
                    Box(Modifier.weight(0.5f)) {
                        (if (state.selectedEndDateMillis != null) state.selectedEndDateMillis?.let {
                            getFormattedDate(
                                it
                            )
                        } else "End Date")?.let { Text(text = it) }
                    }
                    Box(Modifier.weight(0.2f)) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = "ok")
                    }

                }
            },
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                containerColor = Color.Blue,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                weekdayContentColor = Color.Black,
                subheadContentColor = Color.Black,
                yearContentColor = Color.Green,
                currentYearContentColor = Color.Red,
                selectedYearContainerColor = Color.Red,
                disabledDayContentColor = Color.Gray,
                todayDateBorderColor = Color.Blue,
                dayInSelectionRangeContainerColor = Color.LightGray,
                dayInSelectionRangeContentColor = Color.White,
                selectedDayContainerColor = Color.Black
            )
        )
    }
}