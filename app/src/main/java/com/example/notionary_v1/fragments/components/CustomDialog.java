package com.example.notionary_v1.fragments.components;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.notionary_v1.R;

public class CustomDialog {
    private final Dialog dialog;
    private final TextView txtTitle;
    private final ImageView imgIcon;
    private final Button btnCancel;
    private final Button btnOk;

    public CustomDialog(@NonNull Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
        dialog.setContentView(view);

        txtTitle = view.findViewById(R.id.txt_title);
        imgIcon = view.findViewById(R.id.img_icon);
        btnCancel = view.findViewById(R.id.dialog_cancel);
        btnOk = view.findViewById(R.id.dialog_ok);

        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public CustomDialog setTitle(@NonNull String title) {
        txtTitle.setText(title);
        return this;
    }

    public CustomDialog setIcon(@DrawableRes int drawableId) {
        imgIcon.setImageResource(drawableId);
        imgIcon.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomDialog hideIcon() {
        imgIcon.setVisibility(View.GONE);
        return this;
    }

    public CustomDialog setCancelButton(@NonNull String label, @Nullable View.OnClickListener onClick) {
        btnCancel.setText(label);
        btnCancel.setOnClickListener(v -> {
            if (onClick != null) onClick.onClick(v);
            dialog.dismiss();
        });
        return this;
    }

    public CustomDialog setOkButton(@NonNull String label, @Nullable View.OnClickListener onClick) {
        btnOk.setText(label);
        btnOk.setOnClickListener(v -> {
            if (onClick != null) onClick.onClick(v);
            dialog.dismiss();
        });
        return this;
    }

    public void show() {
        dialog.show();
    }
}
