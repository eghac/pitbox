package com.bzgroup.pitboxauxiliovehicular.dialogs;

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

public class DeniedLocationPersmissionDialogFragment extends DialogFragment {

    public static final int DENIED_FIRST_TIME = 0;
    public static final int DENIED_MORE_TIME = 1;
    public static final int DENIED_SETTINGS_FIRST_TIME = 2;

    public interface DeniedLocationPersmissionDialogFragmentListener {
        void onDialogPositiveClick(DialogFragment dialog, int id);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    DeniedLocationPersmissionDialogFragmentListener mListener;

    public DeniedLocationPersmissionDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeniedLocationPersmissionDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view  = inflater.inflate(R.layout.dialog_denied_location_permission, null);
        TextView textView = (TextView)  view.findViewById(R.id.txt_diaglog_permission);
        Bundle bundle = getArguments();
        String body = bundle.getString("DENIED_LOCATION_PERMISSION");
        final int idDialog = bundle.getInt("DENIED_LOCATION_PERMISSION_ID");
        textView.setText(body);
        builder.setView(view)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(DeniedLocationPersmissionDialogFragment.this, idDialog);
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {

//                    }
//                });
        return builder.create();
    }
}
