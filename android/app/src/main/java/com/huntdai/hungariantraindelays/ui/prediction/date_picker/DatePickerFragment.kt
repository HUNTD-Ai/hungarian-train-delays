package com.huntdai.hungariantraindelays.ui.prediction.date_picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.huntdai.hungariantraindelays.ui.prediction.PredictionFragment
import java.io.Serializable
import java.util.Calendar


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEMO", "DATEPICKER oncreate")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        Log.d("DEMO", "DATEPICKER oncreatedialog")
        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val result = DatePickerResult(year, month, day)
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(PredictionFragment.DATE_SELECTED_KEY, result)
    }

    data class DatePickerResult(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int,
    ) : Serializable
}
