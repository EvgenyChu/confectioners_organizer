package ru.churkin.confectioners_organizer.ui.date_picker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun DatePicker(onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onSelect("$dayOfMonth.${month+1}.$year")
        }, year, month, day
    )
    datePickerDialog.setOnDismissListener {
        onDismiss()
    }
    datePickerDialog.show()
}