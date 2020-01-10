package com.bzgroup.pitboxauxiliovehicular.services.modals;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyVehiclesEmptyBottomSheetDialog extends BottomSheetDialogFragment {

    private MyVehiclesEmptyBottomSheetDialogListener mListener;

    @BindView(R.id.activity_services_bottom_sheet_my_vehicle_is_empty_txt)
    TextView activity_services_bottom_sheet_my_vehicle_is_empty_txt;

    public static MyVehiclesEmptyBottomSheetDialog newInstance(String message) {
        MyVehiclesEmptyBottomSheetDialog f = new MyVehiclesEmptyBottomSheetDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("MESSAGE", message);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDismissListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_services_bottom_sheet_my_vehicle_is_empty, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity_services_bottom_sheet_my_vehicle_is_empty_txt
                .setText(getArguments().getString("MESSAGE"));
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @OnClick(R.id.activity_services_bottom_sheet_my_vehicle_is_empty_btn)
    public void handleAddVehicle() {
        if (mListener != null) {
            mListener.addVehicleClick();
            dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MyVehiclesEmptyBottomSheetDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ScheduleBottomSheetDialogListener");
        }

    }

    public interface MyVehiclesEmptyBottomSheetDialogListener {
        void addVehicleClick();

        void onDismissListener();
    }
}
