package com.bzgroup.pitboxauxiliovehicular.services.modals;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

public class ScheduleBottomSheetDialog extends BottomSheetDialogFragment {

    private ScheduleBottomSheetDialogListener mListener;
    Calendar calendar;
    DatePickerDialog pickerDialog;
    TimePickerDialog timePickerDialog;

    String scheduleDate = "";
    String scheduleTime = "";
    String date = null;
    String time = null;
    @BindView(R.id.activity_services_bss_date)
    TextView activity_services_bss_date;
    @BindView(R.id.activity_services_bss_time)
    TextView activity_services_bss_time;
    @BindView(R.id.activity_services_bss_btn)
    Button activity_services_bss_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_services_bottom_sheet_schedule, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    int day;
    int month;
    int year;

    private void setup() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        currentTime = calendar.getTime();
        activity_services_bss_date.setText(convertDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + day + " " + convertToMonth(month + 1));
        activity_services_bss_time.setText(currentTime.getHours() + ":" + currentTime.getMinutes());
        date = day + "-" + convertToMonthIntFormart(month + 1) + "-" + year;
        time = currentTime.getHours() + ":" + convertFormatTime(currentTime.getMinutes());
    }

    public interface ScheduleBottomSheetDialogListener {
        void onScheduleButtonClick(String date, String time);
    }

    Date currentTime;

    @OnClick(R.id.activity_services_bss_date)
    public void handleDate() {
        pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                scheduleDate = convertDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + dayOfMonth + " " + convertToMonth(month + 1);
                date = dayOfMonth + "-" + convertToMonthIntFormart(month + 1) + "-" + year;
                activity_services_bss_date.setText(scheduleDate);
                activity_services_bss_btn.setText(scheduleDate + "  " + scheduleTime);
//                Toast.makeText(getContext(), scheduleTime, Toast.LENGTH_SHORT).show();
            }
        }, year, month, day);
        pickerDialog.show();
    }

    @OnClick(R.id.activity_services_bss_time)
    public void handleTime() {
        if (currentTime != null) {
            timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hour = hourOfDay + ":" + convertFormatTime(minute);
                    activity_services_bss_time.setText(hour);
                    time = hour;
                    scheduleTime = hour;
                    activity_services_bss_btn.setText(scheduleDate + "  " + scheduleTime);
                }
            }, currentTime.getHours(), currentTime.getMinutes(), false);
            timePickerDialog.show();
        } else {
            Toast.makeText(getContext(), "Seleccione primero la fecha para programar el servicio.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.activity_services_bss_btn)
    public void handleSetPickupScheduleService() {
        if (mListener != null) {
            mListener.onScheduleButtonClick(date, time);
            dismiss();
        }
    }

    private String convertFormatTime(int minuto) {
        String s = "" + minuto;
        switch (minuto) {
            case 0:
                s = "00";
                break;
            case 1:
                s = "01";
                break;
            case 2:
                s = "02";
                break;
            case 3:
                s = "03";
                break;
            case 4:
                s = "04";
                break;
            case 5:
                s = "05";
                break;
            case 6:
                s = "06";
                break;
            case 7:
                s = "07";
                break;
            case 8:
                s = "08";
                break;
            case 9:
                s = "09";
                break;
        }
        return s;
    }

    private String convertDayOfWeek(int i) {
        String s = "";
        switch (i) {
            case 1: // Domingo
                s = "Domingo";
                break;
            case 2:
                s = "Lunes";
                break;
            case 3:
                s = "Martes";
                break;
            case 4:
                s = "Miércoles";
                break;
            case 5:
                s = "Jueves";
                break;
            case 6:
                s = "Viernes";
                break;
            case 7:
                s = "Sábado";
                break;
        }
        return s;
    }

    public String convertToMonthIntFormart(int i) {
        String s = String.valueOf(i);
        switch (i) {
            case 1:
                s = "01";
                break;
            case 2:
                s = "02";
                break;
            case 3:
                s = "03";
                break;
            case 4:
                s = "04";
                break;
            case 5:
                s = "05";
                break;
            case 6:
                s = "06";
                break;
            case 7:
                s = "07";
                break;
            case 8:
                s = "08";
                break;
            case 9:
                s = "09";
                break;
            default:
                break;
        }
        return s;
    }

    public String convertToMonth(int i) {
        String s = "";
        switch (i) {
            case 1:
                s = "Enero";
                break;
            case 2:
                s = "Febrero";
                break;
            case 3:
                s = "Marzo";
                break;
            case 4:
                s = "Abril";
                break;
            case 5:
                s = "Mayo";
                break;
            case 6:
                s = "Junio";
                break;
            case 7:
                s = "Julio";
                break;
            case 8:
                s = "Agosto";
                break;
            case 9:
                s = "Septiembre";
                break;
            case 10:
                s = "Octubre";
                break;
            case 11:
                s = "Noviembre";
                break;
            case 12:
                s = "Diciembre";
                break;
        }
        return s;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ScheduleBottomSheetDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ScheduleBottomSheetDialogListener");
        }

    }
}
