package hi.cosmonaut.hourly.picker.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hi.cosmonaut.hourly.ui.theme.HourlyTheme

object TimePicker {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Dialog(
        title: String,
        opened: MutableState<Boolean>,
        initialHour: Int,
        initialMinute: Int,
        is24Hour: Boolean = true,
        onConfirmed: (Int, Int) -> Unit,
        onDismissRequest: () -> Unit,
    ) {

        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = is24Hour
        )

        if (opened.value) {
            HourlyTheme(
                darkTheme = false
            ) {
                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    onDismissRequest = {
                        opened.value = false
                        onDismissRequest()
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(
                                vertical = 16.dp
                            ),
                            text = title,
                            fontWeight = FontWeight.Bold
                        )

                        // time picker
                        TimeInput(state = timePickerState)

                        // buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // dismiss button
                            TextButton(
                                onClick = {
                                    opened.value = false
                                    onDismissRequest()
                                }
                            ) {
                                Text(text = "Dismiss")
                            }

                            // confirm button
                            TextButton(
                                onClick = {
                                    opened.value = false
                                    onConfirmed(timePickerState.hour, timePickerState.minute)
                                }
                            ) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}