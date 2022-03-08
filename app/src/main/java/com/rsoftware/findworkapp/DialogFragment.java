package com.rsoftware.findworkapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    int type_worker;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = "Тип авторизации";
        String button1String = "Ищу работу";
        String button2String = "Работодатель";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Авторизован как работник", Toast.LENGTH_LONG)
                        .show();
                ((MainActivity) getActivity()).employeeClicked();

            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Авторизован как работодатель", Toast.LENGTH_LONG)
                        .show();
                ((MainActivity) getActivity()).employerClicked();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }


    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }
}
