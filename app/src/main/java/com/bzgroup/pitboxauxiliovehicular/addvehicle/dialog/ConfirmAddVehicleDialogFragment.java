package com.bzgroup.pitboxauxiliovehicular.addvehicle.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bzgroup.pitboxauxiliovehicular.R;

public class ConfirmAddVehicleDialogFragment extends DialogFragment {

    public static final int DENIED_FIRST_TIME = 0;
    public static final int DENIED_MORE_TIME = 1;
    public static final int DENIED_SETTINGS_FIRST_TIME = 2;
    public static final String CONFIRM_ADD_VEHICLE_MESSAGE = "CONFIRM_ADD_VEHICLE_MESSAGE";

    public interface ConfirmAddVehicleDialogFragmentListener {
        void onDialogPositiveClick(DialogFragment dialog, int id);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public static ConfirmAddVehicleDialogFragment newInstance(String message) {
        ConfirmAddVehicleDialogFragment b = new ConfirmAddVehicleDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONFIRM_ADD_VEHICLE_MESSAGE, message);
        b.setArguments(bundle);
        return b;
    }

    ConfirmAddVehicleDialogFragmentListener mListener;

    public ConfirmAddVehicleDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ConfirmAddVehicleDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_confirm_add_vehicle, null);
        TextView textView = (TextView) view.findViewById(R.id.txt_diaglog_confirm_add_vehicle);
        Bundle bundle = getArguments();
        String body = bundle.getString(CONFIRM_ADD_VEHICLE_MESSAGE);
        final int idDialog = bundle.getInt("DENIED_LOCATION_PERMISSION_ID");
        textView.setText(body);
        builder.setView(view)
                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ConfirmAddVehicleDialogFragment.this, idDialog);
                    }
                })
                .setNegativeButton("Confimar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ConfirmAddVehicleDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
