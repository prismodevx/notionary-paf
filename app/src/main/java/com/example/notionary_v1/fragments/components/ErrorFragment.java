package com.example.notionary_v1.fragments.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notionary_v1.R;

public class ErrorFragment extends DialogFragment {
    private String errorMessage;

    public ErrorFragment(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(false);

        TextView tvErrorMessage = dialog.findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setText(errorMessage);

        dialog.findViewById(R.id.btnDismiss).setOnClickListener(v -> dismiss());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        return dialog;
    }
}
