package com.bzgroup.pitboxauxiliovehicular.services.modals;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindingSupplierBottomSheetDialog extends BottomSheetDialogFragment {

    private MyVehiclesEmptyBottomSheetDialogListener mListener;

    public static FindingSupplierBottomSheetDialog newInstance(String message) {
        FindingSupplierBottomSheetDialog f = new FindingSupplierBottomSheetDialog();
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
        View v = inflater.inflate(R.layout.activity_services_bottom_sheet_finding_supplier, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
//            mListener = (MyVehiclesEmptyBottomSheetDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ScheduleBottomSheetDialogListener");
        }

    }

    public interface MyVehiclesEmptyBottomSheetDialogListener {
        void addVehicleClick();

        void onDismissListener();
    }
}
