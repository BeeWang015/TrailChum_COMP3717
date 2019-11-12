package ca.bcit.comp3717.trailchum;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calen = Calendar.getInstance();
        int year = calen.get(calen.YEAR);
        int month = calen.get(calen.MONTH);
        int day = calen.get(calen.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),
                year, month, day);
    }
}
