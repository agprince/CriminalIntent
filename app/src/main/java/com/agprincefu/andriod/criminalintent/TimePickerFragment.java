package com.agprincefu.andriod.criminalintent;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by agprincefu on 2018/3/27.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date_time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Context congtext, Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(v).setTitle(R.string.date_time_picker_title).setPositiveButton(android.R.string.ok,null);


        return builder.create();
    }
}
