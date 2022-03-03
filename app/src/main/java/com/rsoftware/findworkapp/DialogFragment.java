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
        String title = "Тип регистрации";
        String button1String = "Я ищу работу";
        String button2String = "Я работодатель";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Вы сделали правильный выбор", Toast.LENGTH_LONG)
                        .show();
                ((MainActivity) getActivity()).employeeClicked();

            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();
                ((MainActivity) getActivity()).employerClicked();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }


    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }
}
