package com.example.gestionvuelos.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public class DatePickerFragment extends DialogFragment {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate c;
        int year, month, day;

        c = LocalDate.now();
        year = c.getYear();
        month = c.getMonthValue()-1;
        day = c.getDayOfMonth();


        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}
